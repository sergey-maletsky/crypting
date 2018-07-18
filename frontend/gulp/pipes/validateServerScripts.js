import gulp from 'gulp'
import { paths, pipes } from '../config'

pipes.validateServerScripts = () => {
  return gulp.src(paths.scriptsDevServer);
  // .pipe(plugins.eslint({
  // 	fix: true
  // }))
  // .pipe(plugins.eslint.format())
  // .pipe(plugins.eslint.failAfterError());
};