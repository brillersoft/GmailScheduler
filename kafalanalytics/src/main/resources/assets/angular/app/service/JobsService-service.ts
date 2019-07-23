import { Injectable } from '@angular/core';
import { RequestOptions, Http } from "@angular/http";
import { Observable } from 'rxjs';
import { of } from 'rxjs/observable/of';
import { OrganizationAdd } from '../employee/admin/organizationadd';
import {HttpResponseObj} from './HttpResponseObj';


var SERVER =   'app/auth/savemultiplefile';
var SERVER_EMP =   'app/auth/savemultiplefileemp';
var SERVER_EMAIL_CONFIG ='app/auth/configureemail';
var SERVER_EMAIL_CONFIG_PST ='app/auth/configureemailpst';

@Injectable()
export class JobsService {

 constructor(private http: Http) { }

 private organizationadd:Object = {};

  runImportRecordsJob(importRecords: any){
  //  var headers = new Headers({"Content-Type": 'application/json; multipart/form-data;'});
    //let options = new RequestOptions({headers: headers });
    let options = new RequestOptions();

    var parentRef = this;

    let formData = new FormData();
    formData.append("file", importRecords.file, importRecords.file.name);
   return this.http.post(SERVER , formData, options).map(
      res => {
        const data = res.json();
        return data;
      }
    ).catch(this.handleError);
    
  }
  
  runImportRecordsJobforEmployee(importRecords: any) {
    //  var headers = new Headers({"Content-Type": 'application/json; multipart/form-data;'});
    //let options = new RequestOptions({headers: headers });
    let options = new RequestOptions();

    let formData = new FormData();
    formData.append("file", importRecords.file, importRecords.file.name);
    return this.http.post(SERVER_EMP, formData, options).map(
      res => {
        const data = res.json();
        return data;
      }
    ).catch(this.handleError);
  }
  
   runImportRecordsJobforEmailconfig(importRecords: any) {
    //  var headers = new Headers({"Content-Type": 'application/json; multipart/form-data;'});
    //let options = new RequestOptions({headers: headers });
    let options = new RequestOptions();

    let formData = new FormData();
    formData.append("file", importRecords.file, importRecords.file.name);
    return this.http.post(SERVER_EMAIL_CONFIG, formData, options).map(
      res => {
        const data = res.json();
        return data;
      }
    ).catch(this.handleError);
  }

  runImportRecordsJobforEmailconfigpst(importRecords: FileList) {
    //  var headers = new Headers({"Content-Type": 'application/json; multipart/form-data;'});
    //let options = new RequestOptions({headers: headers });
    let options = new RequestOptions();
    
    let formData = new FormData();
    //formData.append("file", importRecords.file, importRecords.file.name);
   for (var i = 0; i < importRecords.length; i++) { 
    console.log('Hi its aashish78858  ' + importRecords[i]);
    formData.append("file", importRecords[i]);
 }
 
    return this.http.post(SERVER_EMAIL_CONFIG_PST, formData, options).map(
      res => {
        const data = res.json();
        return data;
      }
    ).catch(this.handleError);
  }

  private handleError(error: Response | any) {
    console.error(error.message || error);
    return Observable.throw(error.message || error);
  }

}