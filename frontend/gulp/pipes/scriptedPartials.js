import { pipes, plugins } from '../config'

pipes.scriptedPartials = () => {
  return pipes.validatePartials()
    .pipe(plugins.htmlhint.failReporter())
    .pipe(plugins.htmlmin({collapseWhitespace: true, removeComments: true}))
    .pipe(plugins.ngHtml2js({
      moduleName: "app"
    }));
};