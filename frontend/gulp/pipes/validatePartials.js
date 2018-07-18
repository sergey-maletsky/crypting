import gulp from 'gulp'
import { paths, pipes, plugins } from '../config'

pipes.validatePartials = () => {
  return gulp.src(paths.partials)
    .pipe(plugins.plumber({ errorHandler: plugins.notify.onError() }))
    .pipe(plugins.htmlhint({'doctype-first': false}))
    .pipe(plugins.htmlhint.reporter());
};