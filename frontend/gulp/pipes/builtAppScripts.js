import gulp from 'gulp'
import { paths, pipes, plugins } from '../config'
import es from 'event-stream'

pipes.builtAppScriptsDev = () => {
  return pipes.validatedAppScripts(paths.scriptsDev)
    .pipe(plugins.newer(paths.distDev))
    .pipe(gulp.dest(paths.distDev));
};

pipes.builtAppScriptsProd = () => {
  const validatedAppScripts = pipes.validatedAppScripts(paths.scriptsProd)
    .pipe(pipes.orderedAppScripts());

  return es.merge(validatedAppScripts)
    .pipe(plugins.sourcemaps.init())
    .pipe(plugins.concat('app.min.js'))
    .pipe(plugins.uglify({mangle: false}))
    .pipe(plugins.sourcemaps.write())
    .pipe(gulp.dest(paths.distProd + '/scripts'))
};