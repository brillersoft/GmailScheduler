import {BaseContainer} from '../BaseContainer';
export class HttpRequestObj {
  public rqBody : BaseContainer;

  constructor(rqObj: BaseContainer) {
        this.rqBody = rqObj;
    }
}
