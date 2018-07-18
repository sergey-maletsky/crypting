import gulp from 'gulp'
import { pipes,plugins } from '../config'

pipes.validatedAppScripts = (scripts) => {
  return gulp.src(scripts)
    .pipe(plugins.plumber({ errorHandler: plugins.notify.onError()}))
    .pipe(plugins.babel())
    // .pipe(plugins.browserify (
    //   {
    //     "transform": ["babelify"],
    //     debug: true
    //   }
    // ))
  // .pipe(plugins.eslint({
  // 	fix: true
  // }))
  // .pipe(plugins.eslint.format())
  // .pipe(plugins.eslint.failAfterError());
};