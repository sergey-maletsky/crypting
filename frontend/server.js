const express = require('express');
const path = require('path');
const fs = require('fs');
const https = require('https');
const proxy = require('http-proxy-middleware');
const cors = require('cors');

const config = require('./server.config');

const remoteHost = 'http://localhost:6060';
const apiPrefix = '/auth';

const options = {
    key: null,
    cert: null
};

options.key = fs.readFileSync(path.join(__dirname, 'sslcert/key.pem'), 'utf8');
options.cert = fs.readFileSync(path.join(__dirname, 'sslcert/server.crt'), 'utf8');

const app = express();

app.options('*', cors());

// Add headers
app.use((req, res, next) => {

    // Website you wish to allow to connect
    res.setHeader('Access-Control-Allow-Origin', '*');

    // Pass to next layer of middleware
    next();
});

app.use(apiPrefix, proxy({
    target: remoteHost,
    onProxyRes: (proxyRes, req, res) => {
        console.log('http://localhost:' + config.port + req.originalUrl + ' -> '
          + remoteHost + req.url)
    }
}));

// static
app.use(express.static(__dirname + '/' + config.directory));

app.get('/*', function (req, res) {
    res.sendFile(path.join(__dirname, config.directory, '/index.html'));
});

https.createServer(options, app).listen(config.port, (req, res) => {
    console.log('HTTPS server on port https://localhost:' + config.port);
});