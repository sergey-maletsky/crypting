import gulp from 'gulp'
import { paths, pipes, plugins } from '../config'

pipes.processedFontsDev = () => {
  return gulp.src(paths.fonts)
    .pipe(plugins.newer(paths.distDev + '/fonts/'))
    .pipe(gulp.dest(paths.distDev + '/fonts/'));
};

pipes.processedFontsProd = () => {
  return gulp.src(paths.fonts)
    .pipe(gulp.dest(paths.distProd + '/fonts/'));
};