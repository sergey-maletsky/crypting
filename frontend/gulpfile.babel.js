import gulp from 'gulp';
import del from 'del';

// == PATH STRINGS ========
import { paths, pipes, plugins } from './gulp/config';

// == PIPES ========
import './gulp/pipes/styles'
import './gulp/pipes/images'
import './gulp/pipes/fonts'
import './gulp/pipes/builtApp'
import './gulp/pipes/builtAppScripts'
import './gulp/pipes/builtVendorScripts'
import './gulp/pipes/builtIndex'
import './gulp/pipes/copySources'
import './gulp/pipes/orderedAppScripts'
import './gulp/pipes/orderedVendorScripts'
import './gulp/pipes/scriptedPartials'
import './gulp/pipes/translates'
import './gulp/pipes/templateCache'
import './gulp/pipes/validateIndex'
import './gulp/pipes/validatedAppScripts'
import './gulp/pipes/validateServerScripts'
import './gulp/pipes/validatePartials'

// == TASKS ========

// checks html source files for syntax errors
gulp.task('validate-partials', pipes.validatePartials);

// checks index.html for syntax errors
gulp.task('validate-index', pipes.validateIndex);

// converts partials to javascript using html2js
gulp.task('convert-partials-to-js', pipes.scriptedPartials);

// runs eslint on the dev server scripts
gulp.task('validate-server-scripts', pipes.validateServerScripts);

// moves app scripts into the dev environment
gulp.task('build-app-scripts-dev', pipes.builtAppScriptsDev);

// concatenates, uglifies, and moves app scripts and partials into the prod environment
gulp.task('build-app-scripts-prod', ['template-cache-prod', 'translate-prod'], pipes.builtAppScriptsProd);

// compiles app sass and moves to the dev environment
gulp.task('build-styles-dev', pipes.builtStylesDev);

// compiles and minifies app sass to css and moves to the prod environment
gulp.task('build-styles-prod', pipes.builtStylesProd);

// moves vendor scripts into the dev environment
gulp.task('build-vendor-scripts-dev', pipes.builtVendorScriptsDev);

// concatenates, uglifies, and moves vendor scripts into the prod environment
gulp.task('build-vendor-scripts-prod', pipes.builtVendorScriptsProd);

// validates and injects sources into index.html and moves it to the dev environment
gulp.task('build-index-dev', pipes.builtIndexDev);

// validates and injects sources into index.html, minifies and moves it to the dev environment
gulp.task('build-index-prod', pipes.builtIndexProd);

// builds a complete dev environment
gulp.task('build-app-dev', pipes.builtAppDev);

// builds a complete prod environment
gulp.task('build-app-prod', pipes.builtAppProd);

// cleans and builds a complete dev environment
gulp.task('clean-build-app-dev', ['clean-dev', 'clean-tmp'], pipes.builtAppDev);

// cleans and builds a complete prod environment
gulp.task('clean-build-app-prod', ['clean-prod', 'clean-tmp', 'template-cache-prod', 'translate-prod'], pipes.builtAppProd);

//build templates for prod environment
gulp.task('template-cache-dev', pipes.templateCacheDev);
gulp.task('template-cache-prod', pipes.templateCacheProd);

gulp.task("translate-dev", pipes.translatesDev);
gulp.task("translate-prod", pipes.translatesProd);

// clean, build, and watch live changes to the dev environment
gulp.task('build-prod', ['clean-build-app-prod', 'validate-server-scripts'], function() {
	del(paths.tmp);
});

//
gulp.task('watch-dev', ['clean-build-app-dev', 'validate-server-scripts'], function () {
  // start nodemon to auto-reload the dev server
  plugins.nodemon({script: 'server.js', ext: 'js', watch: ['devServer/'], env: {NODE_ENV: 'development'}})
    .on('change', ['validate-server-scripts'])
    .on('restart', function () {
      console.log('[nodemon] restarted dev server');
    });

  // start live-reload server
  plugins.livereload.listen({start: true});

  // watch index
  gulp.watch(paths.index, function () {
    return pipes.builtIndexDev()
      .pipe(plugins.livereload());
  });

  // watch sources
  gulp.watch(paths.sources, function () {
    return pipes.copySourcesToDev()
      .pipe(plugins.livereload());
  });

  // watch app scripts
  gulp.watch(paths.scriptsWatch, function () {
    return pipes.builtAppScriptsDev()
      .pipe(plugins.livereload());
  });

  // watch html partials
  gulp.watch(paths.partials, function () {
    return pipes.templateCacheDev()
      .pipe(plugins.livereload());
  });

  // watch styles
  gulp.watch(paths.styles, function () {
    return pipes.builtStylesDev()
      .pipe(plugins.livereload());
  });

  gulp.watch(paths.translates, function() {
    return pipes.translatesDev()
      .pipe(plugins.livereload());
  });
});

//clean
gulp.task('clean-tmp', () => {
  return del.sync(paths.tmp);
});

// removes all compiled dev files
gulp.task('clean-dev', () => {
  return del.sync(paths.distDev);
});

// removes all compiled production files
gulp.task('clean-prod', () => {
  return del.sync(paths.distProd);
});

//
gulp.task('dev-env', function() {
  return process.env.NODE_ENV = 'development';
});

gulp.task('prod-env', function() {
  return process.env.NODE_ENV = 'production';
});

//
gulp.task('start-prod', ['build-prod'], function () {
  // start nodemon to auto-reload the dev server
  plugins.nodemon({script: 'server.js', ext: 'js', watch: ['devServer/'], env: {NODE_ENV: 'production'}})
    .on('change', ['validate-server-scripts'])
    .on('restart', function () {
      console.log('[nodemon] restarted dev server');
    });
});

// default task builds for prod
gulp.task('default', ['clean-build-app-prod']);