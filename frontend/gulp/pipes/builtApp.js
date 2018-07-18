import es from 'event-stream'
import { pipes } from '../config'

pipes.builtAppDev = () => {
  return es.merge(
    pipes.builtIndexDev(),
    pipes.copySourcesToDev(),
    pipes.processedImagesDev(),
    pipes.processedFontsDev()
  );
};

pipes.builtAppProd = () => {
  return es.merge(
    pipes.builtIndexProd(),
    pipes.copySourcesToProd(),
    pipes.processedImagesProd(),
    pipes.processedFontsProd()
  );
};