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
// import { DataService } from "../data.service";
import { FormBuilder, FormArray, FormGroup} from '@angular/forms';


import { JobsService } from '../../service/JobsService-service';


declare var $:any;



var path = require('path');

var _publicPath = path.resolve(__dirname, '../../../../../../webapp');

var _templateURL =   'templates/employee/organizationdetails.html';

@Component({
  selector: 'app-root',
  templateUrl: _templateURL,
 
})

export class OrganizationDetails implements OnInit,OnDestroy,AfterViewInit {
  organizationForm: FormGroup;
 fileToUpload: File = null;
  file: File;
   constructor(private fb: FormBuilder,
     private sharedService:SharedService,
     private router:Router,
    private httpService: HttpService,
    private jobsService: JobsService) {}; 
    
  
   private organizationadd:Object = {};
    ngOnInit() {
      this.organizationForm = this.fb.group({
        companyUrl: '', 
    industryType: '', 
    employees: '',
    address: '', 
    city: '',
    state: '',
    pincode: '',
    country: '',
    organizationAdd     :   this.initOrganizationAdd()
   
  });
  

 
    }
    initOrganizationAdd() : FormGroup
    {
       return this.fb.group({
        companyUrl: '', 
        industryType: '', 
        employees: '',
        address: '', 
        city: '',
        state: '',
        pincode: '',
        country: '',
       });
    }
    
   
    
    saveOrganization(){
      console.log(this.organizationForm.value.organizationAdd);
      var  requestObject = new BaseContainer();   
      
      
           requestObject = this.organizationForm.value.organizationAdd;
           
           
           this.httpService.postRequest<OrganizationAdd>(requestObject,'app/auth/saveorg').subscribe(
                 organizationadd=> {
                 organizationadd.rsBody
                  console.log(requestObject);
                    
                    });
    }
    ngAfterViewInit(){

    }
    ngOnDestroy(){
      
    }
   changRoute(name,tabName)
  {

          this.sharedService.setData({'tabName':tabName});
          this.router.navigate([name]);
  }

  
}
  
  
  
  
  
  
    
