import gulp from 'gulp'
import {paths, pipes, plugins} from '../config'

const argv = require('yargs').argv;

pipes.validateIndex = () => {
    return gulp.src(paths.index)
      .pipe(plugins.htmlhint())
      .pipe(plugins.htmlhint.reporter());
};