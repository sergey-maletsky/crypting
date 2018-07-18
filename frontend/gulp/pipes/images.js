import gulp from 'gulp'
import { paths, pipes, plugins } from '../config'

pipes.processedImagesDev = () => {
  return gulp.src(paths.images)
    .pipe(plugins.newer(paths.distDev + '/images/'))
    .pipe(gulp.dest(paths.distDev + '/images/'));
};

pipes.processedImagesProd = () => {
  return gulp.src(paths.images)
    .pipe(gulp.dest(paths.distProd + '/images/'));
};