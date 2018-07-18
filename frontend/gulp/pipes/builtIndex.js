import gulp from 'gulp'
import { paths, pipes, plugins } from '../config'

pipes.builtIndexDev = () => {
  const orderedVendorScripts = pipes.builtVendorScriptsDev()
    .pipe(pipes.orderedVendorScripts());

  const orderedAppScripts = pipes.builtAppScriptsDev()
    .pipe(pipes.orderedAppScripts());

  const translatesScripts = pipes.translatesDev();

  const templateCache = pipes.templateCacheDev();

  const appStyles = pipes.builtStylesDev();

  return pipes.validateIndex()
    .pipe(gulp.dest(paths.distDev)) // write first to get relative path for inject
    .pipe(plugins.inject(orderedVendorScripts, {relative: true, name: 'bower'}))
    .pipe(plugins.inject(orderedAppScripts, {relative: true}))
    .pipe(plugins.inject(translatesScripts, {relative: true, name: 'translates'}))
    .pipe(plugins.inject(templateCache, {relative: true, name: 'template'}))
    .pipe(plugins.inject(appStyles, {relative: true}))
    .pipe(gulp.dest(paths.distDev));
};

pipes.builtIndexProd = () => {
  const orderedVendorScripts = pipes.builtVendorScriptsProd()
    .pipe(pipes.orderedVendorScripts());

  const orderedAppScripts = pipes.builtAppScriptsProd();

  const vendorStyles = pipes.builtVendorCssProd();

  const appStyles = pipes.builtStylesProd();

  return pipes.validateIndex()
    .pipe(gulp.dest(paths.distProd)) // write first to get relative path for inject
    .pipe(plugins.inject(orderedVendorScripts, {
      relative: true,
      name: 'bower',
      addSuffix: '?ver=' + Date.now()
    }))
    .pipe(plugins.inject(orderedAppScripts, {
      relative: true,
      addSuffix: '?ver=' + Date.now()
    }))
    .pipe(plugins.inject(vendorStyles, {relative: true, name: 'bower'}))
    .pipe(plugins.inject(appStyles, {
      relative: true,
      addSuffix: '?ver=' + Date.now()
    }))
    .pipe(plugins.htmlmin({collapseWhitespace: true, removeComments: true}))
    .pipe(gulp.dest(paths.distProd));
};