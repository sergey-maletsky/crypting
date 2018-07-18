import gulp from 'gulp'
import { paths, pipes, plugins } from '../config'

pipes.translatesDev = () => {
  "use strict";
  return gulp.src(paths.translates)
    .pipe(plugins.newer(paths.distDev + '/js/constants/'))
    .pipe(plugins.yamlMerge('translate.constant.yaml'))
    .pipe(plugins.yaml({space: 4}))
    .pipe(plugins.ngConstant({
      name: 'core',
      deps: false,
      wrap: false
    }))
    .pipe(plugins.insert.wrap(
      ';(function(angular){"use strict";',
      '})(angular);'
    ))
    .pipe(gulp.dest(paths.distDev + '/js/constants'));
};

pipes.translatesProd = () => {
  "use strict";
  return gulp.src(paths.translates)
    .pipe(plugins.yamlMerge('translate.constant.yaml'))
    .pipe(plugins.yaml({space: 4}))
    .pipe(plugins.ngConstant({
      name: 'core',
      deps: false,
      wrap: false
    }))
    .pipe(plugins.insert.wrap(
      ';(function(angular){"use strict";',
      '})(angular);'
    ))
    .pipe(gulp.dest(paths.tmp));
};