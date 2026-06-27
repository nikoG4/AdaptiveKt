const http = require('http');
const fs = require('fs');
const path = require('path');

const dir = path.resolve(process.argv[2]);
const mimeTypes = {
  '.html': 'text/html',
  '.js': 'text/javascript',
  '.wasm': 'application/wasm',
  '.css': 'text/css'
};

const server = http.createServer((req, res) => {
  const url = req.url === '/' || req.url.startsWith('/?') ? '/index.html' : req.url;
  const filePath = path.join(dir, url.split('?')[0]);
  
  fs.readFile(filePath, (err, data) => {
    if (err) {
      res.writeHead(404);
      res.end('Not found');
      return;
    }
    const ext = path.extname(filePath);
    res.writeHead(200, { 'Content-Type': mimeTypes[ext] || 'text/plain' });
    res.end(data);
  });
});

server.listen(8080, () => {
  console.log('Server running on 8080');
});




