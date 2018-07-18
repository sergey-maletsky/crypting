import bowerFiles from 'main-bower-files'
import gulp from 'gulp'
import { paths, pipes, plugins } from '../config'

pipes.builtVendorCssProd = () => {
  return gulp.src(bowerFiles('**/*.css'))
    .pipe(plugins.concat('vendor.min.css'))
    .pipe(plugins.cleanCss())
    .pipe(gulp.dest(paths.distProd + '/styles'));
};

pipes.builtStylesDev = () => {
  return gulp.src(paths.mainStyle)
    .pipe(plugins.plumber({ errorHandler: plugins.notify.onError() }))
    .pipe(plugins.newer(paths.distDev))
    .pipe(plugins.sourcemaps.init())
    .pipe(plugins.sass())
    .pipe(plugins.sourcemaps.write('.'))
    .pipe(gulp.dest(paths.distDev));
};

pipes.builtStylesProd = () => {
  return gulp.src(paths.styles)
    .pipe(plugins.plumber()) // add from dev
    .pipe(plugins.sourcemaps.init())
    .pipe(plugins.sass())
    .pipe(plugins.cleanCss())
    .pipe(plugins.sourcemaps.write())
    .pipe(gulp.dest(paths.distProd));
};