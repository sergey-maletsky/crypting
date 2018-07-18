import gulp from 'gulp'
import { paths, pipes, plugins } from '../config'

pipes.templateCacheDev = () => {
  return gulp.src(paths.templates)
    .pipe(plugins.newer(paths.distDev + '/templates/'))
    .pipe(plugins.angularTemplatecache('templates.module.js',  { module:'templates', standalone:true}))
    .pipe(gulp.dest(paths.distDev + '/templates/'));
};

pipes.templateCacheProd = () => {
  return gulp.src(paths.templates)
  // .pipe(plugins.htmlmin({collapseWhitespace: true, removeComments: true}))
    .pipe(plugins.angularTemplatecache('templates.module.js',  { module:'templates', standalone:true}))
    .pipe(gulp.dest(paths.tmp));
};