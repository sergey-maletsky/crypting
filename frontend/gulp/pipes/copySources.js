import gulp from 'gulp'
import { paths, pipes } from '../config'

pipes.copySourcesToDev = () => {
  return gulp.src(paths.sources)
    .pipe(gulp.dest(paths.distDev));
};

pipes.copySourcesToProd = () => {
  return gulp.src(paths.sources)
    .pipe(gulp.dest(paths.distProd));
};