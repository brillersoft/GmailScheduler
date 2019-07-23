import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders,HttpEvent,HttpResponse} from '@angular/common/http'

import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import { catchError, map, tap } from 'rxjs/operators';

import {BaseContainer} from './../BaseContainer';
import {HttpRequestObj} from './HttpRequestObj';
import {HttpResponseObj} from './HttpResponseObj';

@Injectable()
export class HttpService {

    private  httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };


    constructor(private http: HttpClient) { }

    //POST request
    public postRequest<T extends BaseContainer>(requestObj, url)
    :Observable<T>
    {
        var parentRef = this;
       // $('.spinner').css('display','block');
        var httpRequestObj = new HttpRequestObj(requestObj);

        //create new observable of the return type which will be executed as completed
        //as and when we get back the response from http

        return Observable.create(function subscribe(observer) {
            parentRef.http.post<HttpResponseObj>( url, httpRequestObj,parentRef.httpOptions)
                .pipe(
                    catchError(parentRef.handleError('getHeroes', []))
                )
                .subscribe((httpEvent:HttpResponse<HttpResponseObj>) => {
                    if(httpEvent.errors == null)
                    {
                       // $('.spinner').css('display','none');
                        observer.next(httpEvent);
                        observer.complete();
                    }
                    else
                    {
                        //show validation errors or business errors 
                        observer.complete();
                    }
                });
        });
    }


    private handleError<T> (operation = 'operation', result?: T) {
        return (error: any): Observable<T> => {

            // TODO: send the error to remote logging infrastructure
            console.error(error); // log to console instead

            // TODO: better job of transforming error for user consumption
            //this.log(`${operation} failed: ${error.message}`);

            // Let the app keep running by returning an empty result.
            return of(result as T);
        };
    }

}
