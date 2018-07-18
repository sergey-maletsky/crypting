exports.paths = {
    scriptsDev: [
        'app/**/*.module.js',
        'app/**/*.js'
    ],
    scriptsProd: [
        'app/**/*.module.js',
        './tmp/**/*.module.js',
        'app/**/*.js',
        './tmp/**/*.js'
    ],
    sources: 'app/sources/**/*',
    scriptsWatch: 'app/**/*.js',
    templates: 'app/**/*.html',
    markup: 'app/markup/**/*.html',
    styles: ['./app/**/*.css', './app/**/*.scss'],
    mainStyle: ['./app/styles/main.scss'],
    images: './app/images/**/*',
    fonts: './app/fonts/**/*',
    index: './app/index.html',
    partials: ['app/**/*.html', '!app/index.html'],
    distDev: './dist.dev',
    distProd: './dist.prod',
    scriptsDevServer: 'server.js',
    translates: './app/translations/**/*.yaml',
    tmp: './tmp'
};

exports.pipes = {};

exports.plugins = require('gulp-load-plugins')();