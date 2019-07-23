import { Component ,OnInit,OnDestroy,AfterViewInit} from '@angular/core';
// import {HttpService} from '../../service/http.service';
import {HttpService} from '../../service/http.service';
import { OrganizationAdd } from './organizationadd';
import {BaseContainer} from '../../BaseContainer';
import { SharedService } from '../../service/shared-data-service';
import { ChangeDetectorRef } from '@angular/core';
import { NotifierService } from 'angular-notifier';
import { Router ,ActivatedRoute, Route} from '@angular/router';
// import {BaseContainer} from '../../BaseContainer';
import { DomSanitizer, SafeHtml } from "@angular/platform-browser";
// import { DataService } from "../data.service";
import { FormBuilder, FormArray, FormGroup} from '@angular/forms';

// import { SharedService } from '../../service/shared-data-service';

import { JobsService } from '../../service/JobsService-service';


declare var $:any;



var path = require('path');

var status;

var _publicPath = path.resolve(__dirname, '../../../../../webapp');

var _templateURL =   'templates/employee/admindashboard.html';

@Component({
  selector: 'app-root',
  templateUrl: _templateURL,
})

export class AdminDashboard implements OnInit,OnDestroy,AfterViewInit {
  myForm: FormGroup;
 fileToUpload: File = null;
  file: File;
  private readonly notifier: NotifierService;
  public userId: any = "";
  public parmsData: any = {};

   constructor(private fb: FormBuilder,
     private sharedService:SharedService,
     private router:Router,
    private httpService: HttpService,
    private jobsService: JobsService,
    private route: ActivatedRoute,
    notifierService: NotifierService,
    ) {
      this.notifier = notifierService;
    }; 
    
  
   
    ngOnInit() {
  
      this.parmsData = this.route.snapshot.params;
    this.userId = this.parmsData["userId"];


 
    }
   
  
    
    
    ngAfterViewInit(){

    }
    ngOnDestroy(){
      
    }
   changRoute(name)
  {

          // this.sharedService.setData({'tabName':tabName});
          this.router.navigate([name]);
  }

  changeRouteAdmin(name){
    var userId = this.userId;
    this.router.navigate([name,userId]);
  }
  
   
  
}
  
  
  
  
  
  
    
