import bowerFiles from 'main-bower-files'
import gulp from 'gulp'
import { paths, pipes, plugins } from '../config'

pipes.builtVendorScriptsDev = () => {
  return gulp.src(bowerFiles())
    .pipe(plugins.newer('dist.dev/bower_components'))
    .pipe(gulp.dest('dist.dev/bower_components'));
};

pipes.builtVendorScriptsProd = () => {
  return gulp.src(bowerFiles('**/*.js'))
    .pipe(pipes.orderedVendorScripts())
    .pipe(plugins.concat('vendor.min.js'))
    .pipe(plugins.uglify())
    .pipe(gulp.dest(paths.distProd + '/scripts'));
};