import { Component ,OnInit,OnDestroy,AfterViewInit} from '@angular/core';
// import {HttpService} from '../../service/http.service';
import {HttpService} from '../../service/http.service';
import { OrganizationAdd } from './organizationadd';
import {BaseContainer} from '../../BaseContainer';
import { SharedService } from '../../service/shared-data-service';
import { ChangeDetectorRef } from '@angular/core';

import { Router ,ActivatedRoute, Route} from '@angular/router';
// import {BaseContainer} from '../../BaseContainer';
import { DomSanitizer, SafeHtml } from "@angular/platform-browser";
import {MatTabsModule} from '@angular/material/tabs';

// import { DataService } from "../data.service";
import { FormBuilder, FormArray, FormGroup} from '@angular/forms';


declare var $:any;

var path = require('path');

var _publicPath = path.resolve(__dirname, '../../../../../../webapp');

var _templateURL =   'templates/employee/register.html';


@Component({
  selector: 'app-root',
  templateUrl: _templateURL,
 

})

export class RegisterComponent implements OnInit,OnDestroy,AfterViewInit {
  myForm: FormGroup;
   constructor(private fb: FormBuilder,  
    private httpService: HttpService,
    private router:Router,
    private sharedService:SharedService,
   private matTabsModule: MatTabsModule,
  
   
) {};

  

 

    ngOnInit() {
      
        
        
      
    }
    
   
    
   
    ngAfterViewInit(){
       
    }
    changRoute(name,tabName)
    {
            this.sharedService.setData({'tabName':tabName});
            this.router.navigate([name]);
    }
    ngOnDestroy(){
      
    }

    
}