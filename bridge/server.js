const express = require('express');
const { WebSocketServer } = require('ws');
const { spawn } = require('child_process');
const http = require('http');
const path = require('path');

const PORT = process.env.PORT || 3000;
// Where your Java client jar is — this path is inside the Docker container
const CLIENT_CP = process.env.CLIENT_JAR_CP || '/app/libs/*';
// Where your Java chess server is running
const CHESS_SERVER_URL = process.env.CHESS_SERVER_URL || 'http://chess-server:8080';

const app = express();
const server = http.createServer(app);
const wss = new WebSocketServer({ server, path: '/terminal' });

// Serve the web terminal UI
app.use(express.static(path.join(__dirname, 'web')));

wss.on('connection', (ws) => {
  console.log('Browser connected, spawning Java client...');

  // Spawn one Java client process per browser tab
  const java = spawn('java', [
    `-DSERVER_URL=${CHESS_SERVER_URL}`,
    '-cp', CLIENT_CP,
    'client.ClientMain'
  ], {
    env: {
      ...process.env,
      // Tell the JVM to use ANSI — needed for color codes to work
      TERM: 'xterm-256color',
      FORCE_COLOR: '1',
    }
  });

  let alive = true;

  // Java stdout/stderr → browser terminal
  // Replace bare \n with \r\n so xterm.js doesn't staircase
  const fix = (data) => data.toString().replace(/\r?\n/g, '\r\n');
  java.stdout.on('data', (data) => {
    if (ws.readyState === ws.OPEN) ws.send(fix(data));
  });
  java.stderr.on('data', (data) => {
    if (ws.readyState === ws.OPEN) ws.send(fix(data));
  });

  java.on('close', (code) => {
    alive = false;
    console.log(`Java client exited (code ${code})`);
    if (ws.readyState === ws.OPEN) {
      ws.send(`\r\n\x1b[33mSession ended (exit code ${code}). Refresh to start a new session.\x1b[0m\r\n`);
      ws.close();
    }
  });

  // Browser keystrokes → Java stdin
  ws.on('message', (raw) => {
    if (!alive) return;
    try {
      const msg = JSON.parse(raw);
      if (msg.type === 'input') {
        // xterm.js sends \r for Enter; Java's Scanner needs \n
        java.stdin.write(msg.data.replace(/\r/g, '\n'));
      }
      // resize events are informational only — Java CLI doesn't use them,
      // but xterm.js still sends them so we just ignore them here
    } catch (e) {
      // If it's not JSON, pass it through directly as raw input
      java.stdin.write(raw);
    }
  });

  ws.on('close', () => {
    console.log('Browser disconnected, killing Java client');
    if (alive) java.kill();
  });

  ws.on('error', (err) => {
    console.error('WebSocket error:', err);
    if (alive) java.kill();
  });
});

server.listen(PORT, () => {
  console.log(`Chess terminal bridge running on port ${PORT}`);
});