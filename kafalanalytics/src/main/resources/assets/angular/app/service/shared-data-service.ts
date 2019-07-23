import { Injectable }    from '@angular/core';
import { Observable }     from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

@Injectable()
export class SharedService
{
    data: any;
    dataChange: Observable<any>;
    constructor()
    {}
    setData(data:any)
    {
        this.data = data;
    }
    getData()
    {
       return this.data;
    }
}