import {Component, OnInit, OnDestroy, AfterViewInit} from '@angular/core';
// import {HttpService} from '../../service/http.service';
import {HttpService} from '../../service/http.service';
import {OrganizationAdd} from './organizationadd';
import {BaseContainer} from '../../BaseContainer';
import {SharedService} from '../../service/shared-data-service';
import {ChangeDetectorRef} from '@angular/core';

import {Router, ActivatedRoute, Route} from '@angular/router';
// import {BaseContainer} from '../../BaseContainer';
import {DomSanitizer, SafeHtml} from "@angular/platform-browser";
import {MatTabsModule} from '@angular/material/tabs';

// import { DataService } from "../data.service";
import {FormBuilder, FormArray, FormGroup} from '@angular/forms';
import {JobsService} from '../../service/JobsService-service';
import {NotifierService} from 'angular-notifier';
import { Validators, FormControl} from '@angular/forms';

declare var $: any;

var path = require('path');

var _publicPath = path.resolve(__dirname, '../../../../../../webapp');

var _templateURL = 'templates/employee/emailserver.configuration.html';


@Component({
  selector: 'app-root',
  templateUrl: _templateURL,

})

export class ServerConfiguration implements OnInit, OnDestroy, AfterViewInit {
  myForm: FormGroup;
  outlookFor: FormGroup;
  photoName: any;
photoContent : any;
fileExtension: any;
fileExtensionError: boolean = false;
fileExtensionMessage: any;
gmailStep1:boolean = true;
gmailStep2:boolean = false;
gmailStep3:boolean = false;
gmailStep4:boolean = false;
gmailStep5:boolean = false;
gmailServiceAccountID:String = "";
gmailClientID:String = "";
p12file:boolean = false;
public userId: any = "";
public parmsData: any = {};
gmailFilename: string = "";


  
  
  private readonly notifier: NotifierService;
  constructor(private fb: FormBuilder, private fb1: FormBuilder,
    private httpService: HttpService,
    private router: Router,
    private sharedService: SharedService,
    private matTabsModule: MatTabsModule,
    private route: ActivatedRoute,
    private jobsService: JobsService,
    notifierService: NotifierService, ) {
    this.notifier = notifierService;
    this.gmailStep1 = true;
  
  };
  ngOnInit() {

    this.parmsData = this.route.snapshot.params;
    this.userId = this.parmsData["userId"];

    this.myForm = this.fb.group({
      serviceaccountuser: new FormControl('', [
          Validators.required,
          Validators.pattern("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        ]),
      clientid: new FormControl('',[Validators.required,
        Validators.pattern("^\\d+$")
      ]),
     newPhoto:''

    });

    this.outlookFor = new FormGroup({
      clientid_365: new FormControl('', [Validators.required
        ]),
        azure_ad_admin_user_name: new FormControl('',[Validators.required
      ]),
      azure_ad_admin_password: new FormControl('',[Validators.required
      ])

    });






  }
  ngAfterViewInit() {

    // console.log(this.myFormoutlook.valid);
    this.getGmailServerInfo();
    

  }
  changRoute(name, tabName) {
    this.sharedService.setData({'tabName': tabName});
    this.router.navigate([name]);
  }
  ngOnDestroy() {

  }


  
  saveGmailConfig() {
    // console.log("kjjk---" + JSON.stringify(this.myForm.value.serviceaccountuser, null, 2));
    // console.log("kjjk525252552---" + JSON.stringify(this.myForm.value.clientid, null, 2));
    var requestObject = new BaseContainer();
    requestObject = this.myForm.value.serviceaccountuser + "/" + this.myForm.value.clientid;


    this.httpService.postRequest<OrganizationAdd>(requestObject, 'app/auth/saveemailconfvalues').subscribe(
      organizationadd => {
        console.log("Reply");
        // console.log(organizationadd.rsBody.msg);
        // console.log(organizationadd.rsBody.result);
        if (organizationadd.rsBody.result == 'success') {
          if (organizationadd.rsBody.msg == "101") {
            console.log("Hello Sucess");
            // this.notifier.notify('success', 'Data has been updated successfully!');
            this.myForm.reset();
            this.getGmailServerInfo();
          } else if (organizationadd.rsBody.msg == "500") {
            // this.notifier.notify('error', 'Error Occured!');
            this.getGmailServerInfo();
            this.myForm.reset();
          } else {
            // this.notifier.notify('success', organizationadd.rsBody.msg);
            this.getGmailServerInfo();
            this.myForm.reset();
          }
        }
        else {
          // this.notifier.notify('error', "Some Error Occured! Try Again!");
        }

      });
      this.gmailNext();
  }


  saveOutlookConfig() {
    console.log("kjjk---" + JSON.stringify(this.outlookFor.value.clientid_365, null, 2));
    console.log("kjjk525252552---" + JSON.stringify(this.outlookFor.value.azure_ad_admin_user_name, null, 2));
    console.log("kjjk525252552---" + JSON.stringify(this.outlookFor.value.azure_ad_admin_password, null, 2));
    var requestObject = new BaseContainer();
    requestObject = this.outlookFor.value.clientid_365 + "/" + this.outlookFor.value.azure_ad_admin_user_name+"/"+this.outlookFor.value.azure_ad_admin_password;


    this.httpService.postRequest<OrganizationAdd>(requestObject, 'app/auth/saveemailconfvaluesoutlook').subscribe(
      organizationadd => {
        console.log("Reply");
        console.log(organizationadd.rsBody.msg);
        console.log(organizationadd.rsBody.result);
        if (organizationadd.rsBody.result == 'success') {
          if (organizationadd.rsBody.msg == "101") {
            console.log("Hello Sucess");
            // this.notifier.notify('success', 'Data has been updated successfully!');
          } else if (organizationadd.rsBody.msg == "500") {
            this.notifier.notify('error', 'Error Occured!');
          } else {
            this.notifier.notify('success', organizationadd.rsBody.msg);
          }
        }
        else {
          this.notifier.notify('error', "Some Error Occured! Try Again!");
        }

      });
  }


  changeRouteAdmin(name){
    var userId = this.userId;
    this.router.navigate([name,userId]);
  }




  runImportRecordsJob($event) {
    // try{
    var files = $event.target.files;
    var fileToUpload: File = files.item(0);
    // }catch(err){
    //   var fileToUpload: File = new File([""], "filename");
    // }
    
  
if(fileToUpload.name != ""){
    this.photoName = fileToUpload.name;
    
    
     var allowedExtensions = ["p12"];
    this.fileExtension = this.photoName.split('.').pop();
 console.log('Hi fileExtension is  ' +this.fileExtension);
    if(this.isInArray(allowedExtensions, this.fileExtension)) {
        this.fileExtensionError = false;
        this.fileExtensionMessage = ""
// some Changes.
console.log('This is in if');
if (confirm("Are you sure to upload the file.")) {
  //  Checking the existance of file
  var requestObject = new BaseContainer();
  requestObject = "";
  this.jobsService.runImportRecordsJobforEmailconfig({file: fileToUpload}).subscribe(
    success => {
      console.log('Uploading file succefully.2..!' + JSON.stringify(success));
      if (success.rsBody.result == 'success') {
        if (success.rsBody.msg == "101") {

          // this.notifier.notify('success', 'Data has been updated successfully!');
        } else if (success.rsBody.msg == "500") {
          // this.notifier.notify('error', 'Error Occured!');
        } else {
          // this.notifier.notify('success', success.rsBody.msg);
        }
      }
      else {
        // this.notifier.notify('error', "Some Error Occured! Try Again!");
      }
    },
   error => console.log(error),
  );

}
// end of some changes.


    } else {
      console.log('This is in else');
        this.fileExtensionMessage = "This Format Is Not Supported."
        this.fileExtensionError = true;
        this.myForm.invalid;
    }

    if (fileToUpload) {
        var reader = new FileReader();
        reader.onloadend = (e: any) => {
            var contents = e.target.result;
            this.photoContent = contents;
        }
        reader.readAsDataURL(fileToUpload);
    } else {
        alert("Failed to load file");
    }
    if (this.fileExtensionError) {
      console.log('Hi fileExtensionError ' + this.fileExtensionError);
    }
    
  }
  

  }
  
    runImportRecordsJobforfolder(files: FileList) {
      const uplodedfile = files;
       for (let i = 0; i < files.length; i++) {
        const uplodedfile = files[i];
        console.log('Hi its aashish120  ' +uplodedfile);
       }
        // Excecuting the process for the single file.
        
        this.jobsService.runImportRecordsJobforEmailconfigpst(files).subscribe(
          success => {
            console.log('Uploading file succefully.2..!' + JSON.stringify(success));
            if (success.rsBody.result == 'success') {
              if (success.rsBody.msg == "101") {
      
                // this.notifier.notify('success', 'Data has been updated successfully!');
              } else if (success.rsBody.msg == "500") {
                // this.notifier.notify('error', 'Error Occured!');
              } else {
                // this.notifier.notify('success', success.rsBody.msg);
              }
            }
            else {
              // this.notifier.notify('error', "Some Error Occured! Try Again!");
            }
          },
        )
        
       
   
    
    }

    getGmailServerInfo(){

      var requestObject = new BaseContainer();

      console.log("Server Info")
      this.httpService.postRequest<OrganizationAdd>(requestObject, 'app/auth/getGmailInfo').subscribe(
        organizationadd => {
          // console.log(organizationadd.rsBody.result);
          if (organizationadd.rsBody.result == 'success') {

              console.log(organizationadd.rsBody.msg);
              this.gmailServiceAccountID = organizationadd.rsBody.msg.serviceAccountUser;
              this.gmailClientID = organizationadd.rsBody.msg.clientId;
              this.p12file = organizationadd.rsBody.msg.fileAvaialable;
              if(this.p12file == true){
                this.gmailStep2 = false;
                this.gmailStep1 = false;
                this.gmailStep3 = false;
                this.gmailStep4 = false;
                this.gmailStep5 = true;
              }
          }
        });

    }

    gmailNext(){
      if(this.gmailStep1 == true){
        this.gmailStep2 = true;
        this.gmailStep1 = false;
        this.gmailStep3 = false;
        this.gmailStep4 = false;
        this.gmailStep5 = false;
      }else if(this.gmailStep2 == true){
        this.gmailStep2 = false;
        this.gmailStep1 = false;
        this.gmailStep3 = true;
        this.gmailStep4 = false;
        this.gmailStep5 = false;
      }else if(this.gmailStep3 == true){
        this.gmailStep2 = false;
        this.gmailStep1 = false;
        this.gmailStep3 = false;
        this.gmailStep4 = true;
        this.gmailStep5 = false;
      }else if(this.gmailStep4 == true){
        this.gmailStep2 = false;
        this.gmailStep1 = false;
        this.gmailStep3 = false;
        this.gmailStep4 = false;
        this.gmailStep5 = true;
        // this.getGmailServerInfo();
      }
    }

    gmailBack(){
      if(this.gmailStep2 == true){
        this.gmailStep2 = false;
        this.gmailStep1 = true;
        this.gmailStep3 = false;
        this.gmailStep4 = false;
        this.gmailStep5 = false;
      }else if(this.gmailStep3 == true){
        this.gmailStep2 = true;
        this.gmailStep1 = false;
        this.gmailStep3 = false;
        this.gmailStep4 = false;
        this.gmailStep5 = false;
      }else if(this.gmailStep4 == true){
        this.gmailStep2 = false;
        this.gmailStep1 = false;
        this.gmailStep3 = true;
        this.gmailStep4 = false;
        this.gmailStep5 = false;
      }else if(this.gmailStep5 == true){
        this.gmailStep2 = false;
        this.gmailStep1 = false;
        this.gmailStep3 = false;
        this.gmailStep4 = true;
        this.gmailStep5 = false;
        
      }
    }


    getGmailStep1Background(){
      if(this.gmailStep1 == true){
        return '#17bbc5';
      }else if(this.gmailStep2 == true || this.gmailStep3 == true || this.gmailStep4 == true){
        return 'grey';
      }else{
        return 'white';
      }
    }

    getGmailStep2Background(){
      if(this.gmailStep2 == true){
        return '#17bbc5';
      }else if(this.gmailStep3 == true || this.gmailStep4 == true){
        return 'grey';
      }else{
        return 'white';
      }
    }

    getGmailStep2Color(){
      if(this.gmailStep1 == true){
        return '#17bbc5';
      }else{
        return 'white';
      }
    }

    getGmailStep3Background(){
      if(this.gmailStep3 == true){
        return '#17bbc5';
      }else if(this.gmailStep4 == true){
        return 'grey';
      }else{
        return 'white';
      }
    }

    getGmailStep3Color(){
      if(this.gmailStep1 == true || this.gmailStep2 == true){
        return '#17bbc5';
      }else{
        return 'white';
      }
    }

    getGmailStep4Background(){
      if(this.gmailStep4 == true){
        return '#17bbc5';
      }else{
        return 'white';
      }
    }

    getGmailStep4Color(){
      if(this.gmailStep1 == true || this.gmailStep2 == true || this.gmailStep3 == true){
        return '#17bbc5';
      }else{
        return 'white';
      }
    }

    getGmailStep2Border(){
      if(this.gmailStep1 == true ){
        return '1px solid #17bbc5';
      }else{
        return 'white';
      }
    }

    getGmailStep3Border(){
      if(this.gmailStep1 == true || this.gmailStep2 == true ){
        return '1px solid #17bbc5';
      }else{
        return 'white';
      }
    }

    getGmailStep4Border(){
      if(this.gmailStep1 == true || this.gmailStep2 == true || this.gmailStep3 == true){
        return '1px solid #17bbc5';
      }else{
        return 'white';
      }
    }

    backMargin(){
      if(this.gmailStep4 == true){
        return '80%';
      }else{
        return '60%';
      }
    }

    reset(){

      this.gmailStep1 = true;
      this.gmailStep2 = false;
      this.gmailStep3 = false;
      this.gmailStep4 = false;
      this.gmailStep5 = false;

    }

    changeSettings(){

      this.gmailStep1 = false;
      this.gmailStep2 = false;
      this.gmailStep3 = false;
      this.gmailStep4 = true;
      this.gmailStep5 = false;

    }


/*- checks if word exists in array -*/
 isInArray(array, word) {
    return array.indexOf(word.toLowerCase()) > -1;
}
  

  }
