import { pipes, plugins } from '../config'

pipes.orderedAppScripts = () => {
  return plugins.angularFilesort();
};