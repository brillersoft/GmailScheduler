import { Component ,OnInit,OnDestroy,AfterViewInit} from '@angular/core';
import {HttpService} from '../../service/http.service';
import { Router ,ActivatedRoute} from '@angular/router';
import * as d3 from "d3";
import { Dashboard } from './dashboard';
import {BaseContainer} from '../../BaseContainer';
import { SharedService } from '../../service/shared-data-service';
import { ChangeDetectorRef } from '@angular/core';
declare var $:any;

var path = require('path');

var _publicPath = path.resolve(__dirname, '../../../../../../webapp');

var _templateURL =   'templates/employee/dashboard.component.html' ;

@Component({
  selector: 'app-root',
  templateUrl: _templateURL
})
export class DashboardComponent implements OnInit,OnDestroy,AfterViewInit {

  
    private resData:Object = {};
   private empTonsrc:any = {};
   private clientTonsrc:any = {};
   public empId:Object = {};
    private dashboard:Object = {};
    public teamRelation:any = {};
    public clientRelation:any = {};
    private resList:any = [];
    public resclient:any = [];
    public companyName:string = "";
    public companyIndex:number = 0;
  constructor(
    private httpService: HttpService,
    private router:Router,
    private sharedService:SharedService,
    private cdRef:ChangeDetectorRef)
  {}
  
  
  drawLeftSideComponents()
{
var arc = d3.arc();
d3.select('#outerarc-left')
      .attr('transform', 'translate('+[250,250]+')')
      .attr('d', arc({
          innerRadius: 95,
          outerRadius: 105,
          startAngle: -1*Math.PI,
          endAngle: 0,
      })).style('fill','#e0e0e0');

  d3.select('#personbody-left')
                .attr('transform', 'translate('+[250,290]+')')
                .attr('d', arc({
                    innerRadius: 29.99,
                    outerRadius: 30,
                    startAngle: -Math.PI*0.5,
                    endAngle: Math.PI*0.5
                })).style('stroke','#01579b');


                    d3.select('#bluetextarc-left')
                                  .attr('transform', 'translate('+[425,250]+')')
                                  .attr('d', arc({
                                      innerRadius: 0,
                                      outerRadius: 14.5,
                                      startAngle: 0,
                                      endAngle: Math.PI
                                  })).style('fill','#01579b');


      //calculate the values for each angle in degrees for the outer circles
      var degrees = [90,120,150,180,210,240,270];
        var text_degrees = [90,120,150,180,210,240,270];
      for(var count=0; count < degrees.length; count++)
      {
           d3.select('#outercircle-left-' + degrees[count]).attr('r',25)
              .attr('cx',(155*Math.cos((degrees[count]/180)*Math.PI)) + 250)
              .attr('cy', (155*Math.sin((degrees[count]/180)*Math.PI)) + 250);
        
//        d3.select('#outercircle-text-left-' + degrees[count]).attr('r',25)
//        .attr('x',(155*Math.cos((text_degrees[count]/180)*Math.PI)) + 250)  
//              .attr('y', (155*Math.sin((text_degrees[count]/180)*Math.PI)) + 250);

              d3.select('#outerarc-left-' + degrees[count]).attr('r',5)
              .attr('cx',(105*Math.cos((degrees[count]/180)*Math.PI)) + 250)
              .attr('cy', (105*Math.sin((degrees[count]/180)*Math.PI)) + 250);

              /*d3.select('#text-left-' + text_degrees[count])
              .attr('x',(210*Math.cos((text_degrees[count]/180)*Math.PI)) + 250)
              .attr('y', (200*Math.sin((text_degrees[count]/180)*Math.PI)) + 250);*/
      }
    }

    drawTopSideComponents(){
        var arc = d3.arc();
        d3.select('#outerarc-top')
            .attr('transform', 'translate('+[150,250]+')')
            .attr('d', arc({
                innerRadius: 60,
                outerRadius: 70,
                startAngle: (-1*Math.PI)/2,
                endAngle: Math.PI/2,
            })).style('fill','#e0e0e0');

            d3.select('#personbody-top')
                    .attr('transform', 'translate('+[150,290]+')')
                    .attr('d', arc({
                        innerRadius: 29.99,
                        outerRadius: 30,
                        startAngle: (-Math.PI*0.5)+0.5,
                        endAngle: (Math.PI*0.5)-0.5
                    })).style('stroke','#01579b');

            var degrees = [180,210,240,270,300,330,360];
            var text_degrees = [85,55,25,0,325,295,265];
            for(var count=0; count < degrees.length; count++)
            {
                    d3.select('#outercircle-top-' + degrees[count]).attr('r',20)
                    .attr('cx',(100*Math.cos((degrees[count]/180)*Math.PI)) + 150)
                    .attr('cy', (100*Math.sin((degrees[count]/180)*Math.PI)) + 250);
    //            d3.select('#outercircle-text-right-' + degrees[count]).attr('r',25)
    //        .attr('x',(155*Math.cos((text_degrees[count]/180)*Math.PI)) + 250)
    //              .attr('y', (155*Math.sin((text_degrees[count]/180)*Math.PI)) + 250);
    
                    // d3.select('#outerarc-top-' + degrees[count]).attr('r',5)
                    // .attr('cx',(100*Math.cos((degrees[count]/180)*Math.PI)) + 170)
                    // .attr('cy', (100*Math.sin((degrees[count]/180)*Math.PI)) + 250);
    
                    /* d3.select('#text-right-' + text_degrees[count])
                    .attr('x',(185*Math.cos((text_degrees[count]/180)*Math.PI)) + 250)
                    .attr('y', (185*Math.sin((text_degrees[count]/180)*Math.PI)) + 250);*/
            }

    }

    drawBottomSideComponents(){
        var arc = d3.arc();
        d3.select('#outerarc-bottom')
        .attr('transform', 'translate('+[150,250]+')')
        .attr('d', arc({
            innerRadius: 60,
            outerRadius: 70,
            startAngle: Math.PI/2,
            endAngle: (-1*Math.PI)/2,
        })).style('fill','#e0e0e0');

        d3.select('#personbody-top')
                .attr('transform', 'translate('+[150,290]+')')
                .attr('d', arc({
                    innerRadius: 29.99,
                    outerRadius: 30,
                    startAngle: (-Math.PI*0.5)+0.5,
                    endAngle: (Math.PI*0.5)-0.5
                })).style('stroke','#01579b');

        var degrees = [180,150,120,90,60,30,0];
        var text_degrees = [85,55,25,0,325,295,265];
        for(var count=0; count < degrees.length; count++)
        {
                d3.select('#outercircle-bottom-' + degrees[count]).attr('r',20)
                .attr('cx',(100*Math.cos((degrees[count]/180)*Math.PI)) + 150)
                .attr('cy', (100*Math.sin((degrees[count]/180)*Math.PI)) + 100);
//            d3.select('#outercircle-text-right-' + degrees[count]).attr('r',25)
//        .attr('x',(155*Math.cos((text_degrees[count]/180)*Math.PI)) + 250)
//              .attr('y', (155*Math.sin((text_degrees[count]/180)*Math.PI)) + 250);

                // d3.select('#outerarc-top-' + degrees[count]).attr('r',5)
                // .attr('cx',(100*Math.cos((degrees[count]/180)*Math.PI)) + 170)
                // .attr('cy', (100*Math.sin((degrees[count]/180)*Math.PI)) + 250);

                /* d3.select('#text-right-' + text_degrees[count])
                .attr('x',(185*Math.cos((text_degrees[count]/180)*Math.PI)) + 250)
                .attr('y', (185*Math.sin((text_degrees[count]/180)*Math.PI)) + 250);*/
        }
    }

    

    drawRightSideComponents()
    {
    var arc = d3.arc();
    d3.select('#outerarc-right')
          .attr('transform', 'translate('+[250,250]+')')
          .attr('d', arc({
              innerRadius: 95,
              outerRadius: 105,
              startAngle: 0,
              endAngle: Math.PI,
          })).style('fill','#e0e0e0');

      d3.select('#personbody-right')
                    .attr('transform', 'translate('+[250,290]+')')
                    .attr('d', arc({
                        innerRadius: 29.99,
                        outerRadius: 30,
                        startAngle: -Math.PI*0.5,
                        endAngle: Math.PI*0.5
                    })).style('stroke','#01579b');


                        d3.select('#bluetextarc-right')
                                      .attr('transform', 'translate('+[75,250]+')')
                                      .attr('d', arc({
                                          innerRadius: 0,
                                          outerRadius: 14.5,
                                          startAngle: -Math.PI,
                                          endAngle: 0
                                      })).style('fill','#01579b');


          //calculate the values for each angle in degrees for the outer circles
          var degrees = [90,60,30,0,330,300,270];
          var text_degrees = [85,55,25,0,325,295,265];
          for(var count=0; count < degrees.length; count++)
          {
               d3.select('#outercircle-right-' + degrees[count]).attr('r',25)
                  .attr('cx',(155*Math.cos((degrees[count]/180)*Math.PI)) + 250)
                  .attr('cy', (155*Math.sin((degrees[count]/180)*Math.PI)) + 250);
//            d3.select('#outercircle-text-right-' + degrees[count]).attr('r',25)
//        .attr('x',(155*Math.cos((text_degrees[count]/180)*Math.PI)) + 250)
//              .attr('y', (155*Math.sin((text_degrees[count]/180)*Math.PI)) + 250);

                  d3.select('#outerarc-right-' + degrees[count]).attr('r',5)
                  .attr('cx',(105*Math.cos((degrees[count]/180)*Math.PI)) + 250)
                  .attr('cy', (105*Math.sin((degrees[count]/180)*Math.PI)) + 250);

                 /* d3.select('#text-right-' + text_degrees[count])
                  .attr('x',(185*Math.cos((text_degrees[count]/180)*Math.PI)) + 250)
                  .attr('y', (185*Math.sin((text_degrees[count]/180)*Math.PI)) + 250);*/
          }
        }

        drawRightSideTabletComponents()
        {
        var arc = d3.arc();
        d3.select('#outerarc-rightT')
              .attr('transform', 'translate('+[125,250]+')')
              .attr('d', arc({
                  innerRadius: 95,
                  outerRadius: 105,
                  startAngle: 0,
                  endAngle: Math.PI,
              })).style('fill','#e0e0e0');
    
          d3.select('#personbody-rightT')
                        .attr('transform', 'translate('+[125,290]+')')
                        .attr('d', arc({
                            innerRadius: 29.99,
                            outerRadius: 30,
                            startAngle: -Math.PI*0.5,
                            endAngle: Math.PI*0.5
                        })).style('stroke','#01579b');
    
    
                            // d3.select('#bluetextarc-right')
                            //               .attr('transform', 'translate('+[75,250]+')')
                            //               .attr('d', arc({
                            //                   innerRadius: 0,
                            //                   outerRadius: 14.5,
                            //                   startAngle: -Math.PI,
                            //                   endAngle: 0
                            //               })).style('fill','#01579b');
    
    
              //calculate the values for each angle in degrees for the outer circles
              var degrees = [90,60,30,0,330,300,270];
              var text_degrees = [85,55,25,0,325,295,265];
              for(var count=0; count < degrees.length; count++)
              {
                   d3.select('#outercircle-rightT-' + degrees[count]).attr('r',25)
                      .attr('cx',(155*Math.cos((degrees[count]/180)*Math.PI)) + 125)
                      .attr('cy', (155*Math.sin((degrees[count]/180)*Math.PI)) + 250);
    //            d3.select('#outercircle-text-right-' + degrees[count]).attr('r',25)
    //        .attr('x',(155*Math.cos((text_degrees[count]/180)*Math.PI)) + 250)
    //              .attr('y', (155*Math.sin((text_degrees[count]/180)*Math.PI)) + 250);
    
                      d3.select('#outerarc-rightT-' + degrees[count]).attr('r',5)
                      .attr('cx',(105*Math.cos((degrees[count]/180)*Math.PI)) + 125)
                      .attr('cy', (105*Math.sin((degrees[count]/180)*Math.PI)) + 250);
    
                     /* d3.select('#text-right-' + text_degrees[count])
                      .attr('x',(185*Math.cos((text_degrees[count]/180)*Math.PI)) + 250)
                      .attr('y', (185*Math.sin((text_degrees[count]/180)*Math.PI)) + 250);*/
              }
            }
  ngOnInit() {
    
    
    this.drawLeftSideComponents();
    this.drawRightSideComponents();
    this.drawTopSideComponents();
    this.drawBottomSideComponents();
    this.drawRightSideTabletComponents()
    if(this.sharedService.getData() != undefined && this.sharedService.getData().companyName != undefined && this.sharedService.getData().companyIndex != undefined){
        this.companyName = this.sharedService.getData().companyName;
        this.companyIndex = this.sharedService.getData().companyIndex;
        //   console.log(this.sharedService.getData().companyIndex);
        console.log(this.companyName);
          if(this.companyName == undefined || this.companyName == null){
              this.companyName = "";
          }
          if(this.companyIndex == undefined || this.companyIndex == null){
            this.companyIndex = 0;
        }
    }
    
  }

  ngAfterViewInit(){
           
           
           var  requestObj = new BaseContainer();
           
        if(this.companyName != undefined && this.companyName != null && this.companyName != ""){
            requestObj['companyName'] = this.companyName;
            this.httpService.postRequest<Dashboard>(requestObj,'app/auth/dislaydashboard1').subscribe(
                dashboard=> {
                    if(dashboard.rsBody.result=='success')
                    {
                        this.dashboard = dashboard.rsBody.msg;
                        this.teamRelation = this.dashboard['relationWithTeam'];
                        this.clientRelation = this.dashboard['relationWithClient'];
                        this.empId =  this.dashboard['employeeId'];
                         this.empTonsrc = this.dashboard['toneOfTeamMail'];
                          this.clientTonsrc = this.dashboard['toneOfClientMail'];
                      
                d3.select("#outercircle-text-left-90").text(this.clientTonsrc["analytical"] + "%");
                d3.select("#outercircle-text-left-120").text(this.clientTonsrc["confident"] + "%");
                d3.select("#outercircle-text-left-150").text(this.clientTonsrc["tentative"] + "%");
                d3.select("#outercircle-text-left-180").text(this.clientTonsrc["fear"] + "%");
                d3.select("#outercircle-text-left-210").text(this.clientTonsrc["sadness"] + "%");
                d3.select("#outercircle-text-left-240").text(this.clientTonsrc["joy"] + "%");
                d3.select("#outercircle-text-left-270").text(this.clientTonsrc["anger"] + "%");
                
                d3.select("#outercircle-text-right-90").text(this.clientTonsrc["analytical"] + "%");
                d3.select("#outercircle-text-right-60").text(this.clientTonsrc["confident"] + "%");
                d3.select("#outercircle-text-right-30").text(this.clientTonsrc["tentative"] + "%");
                d3.select("#outercircle-text-right-0").text(this.clientTonsrc["fear"] + "%");
                d3.select("#outercircle-text-right-330").text(this.clientTonsrc["sadness"] + "%");
                d3.select("#outercircle-text-right-300").text(this.clientTonsrc["joy"] + "%");
                d3.select("#outercircle-text-right-270").text(this.clientTonsrc["anger"] + "%");

                d3.select("#outercircle-text-rightT-90").text(this.clientTonsrc["analytical"] + "%");
                d3.select("#outercircle-text-rightT-60").text(this.clientTonsrc["confident"] + "%");
                d3.select("#outercircle-text-rightT-30").text(this.clientTonsrc["tentative"] + "%");
                d3.select("#outercircle-text-rightT-0").text(this.clientTonsrc["fear"] + "%");
                d3.select("#outercircle-text-rightT-330").text(this.clientTonsrc["sadness"] + "%");
                d3.select("#outercircle-text-rightT-300").text(this.clientTonsrc["joy"] + "%");
                d3.select("#outercircle-text-rightT-270").text(this.clientTonsrc["anger"] + "%");
           
                d3.select("#outercircle-text-top-180").text(this.clientTonsrc["analytical"] + "%");
                d3.select("#outercircle-text-top-210").text(this.clientTonsrc["confident"] + "%");
                d3.select("#outercircle-text-top-240").text(this.clientTonsrc["tentative"] + "%");
                d3.select("#outercircle-text-top-270").text(this.clientTonsrc["fear"] + "%");
                d3.select("#outercircle-text-top-300").text(this.clientTonsrc["sadness"] + "%");
                d3.select("#outercircle-text-top-330").text(this.clientTonsrc["joy"] + "%");
                d3.select("#outercircle-text-top-360").text(this.clientTonsrc["anger"] + "%");
    
                d3.select("#outercircle-text-bottom-180").text(this.clientTonsrc["anlaytical"] + "%");
                d3.select("#outercircle-text-bottom-150").text(this.clientTonsrc["confident"] + "%");
                d3.select("#outercircle-text-bottom-120").text(this.clientTonsrc["tentative"] + "%");
                d3.select("#outercircle-text-botton-90").text(this.clientTonsrc["fear"] + "%");
                d3.select("#outercircle-text-bottom-60").text(this.clientTonsrc["sadness"] + "%");
                d3.select("#outercircle-text-bottom-30").text(this.clientTonsrc["joy"] + "%");
                d3.select("#outercircle-text-bottom-0").text(this.clientTonsrc["anger"] + "%");
           
                    }
                });
        }else{
        this.httpService.postRequest<Dashboard>(this.dashboard,'app/auth/dislaydashboard').subscribe(
            dashboard=> {
                if(dashboard.rsBody.result=='success')
                {
                    this.dashboard = dashboard.rsBody.msg;
                    this.teamRelation = this.dashboard['relationWithTeam'];
                    this.clientRelation = this.dashboard['relationWithClient'];
                    this.empId =  this.dashboard['employeeId'];
                     this.empTonsrc = this.dashboard['toneOfTeamMail'];
                      this.clientTonsrc = this.dashboard['toneOfClientMail'];
                  
            d3.select("#outercircle-text-left-90").text(this.empTonsrc["analytical"] + "%");
            d3.select("#outercircle-text-left-120").text(this.empTonsrc["confident"] + "%");
            d3.select("#outercircle-text-left-150").text(this.empTonsrc["tentative"] + "%");
            d3.select("#outercircle-text-left-180").text(this.empTonsrc["fear"] + "%");
            d3.select("#outercircle-text-left-210").text(this.empTonsrc["sadness"] + "%");
            d3.select("#outercircle-text-left-240").text(this.empTonsrc["joy"] + "%");
            d3.select("#outercircle-text-left-270").text(this.empTonsrc["anger"] + "%");
            
            d3.select("#outercircle-text-right-90").text(this.clientTonsrc["analytical"] + "%");
            d3.select("#outercircle-text-right-60").text(this.clientTonsrc["confident"] + "%");
            d3.select("#outercircle-text-right-30").text(this.clientTonsrc["tentative"] + "%");
            d3.select("#outercircle-text-right-0").text(this.clientTonsrc["fear"] + "%");
            d3.select("#outercircle-text-right-330").text(this.clientTonsrc["sadness"] + "%");
            d3.select("#outercircle-text-right-300").text(this.clientTonsrc["joy"] + "%");
            d3.select("#outercircle-text-right-270").text(this.clientTonsrc["anger"] + "%");

            d3.select("#outercircle-text-rightT-90").text(this.clientTonsrc["analytical"] + "%");
            d3.select("#outercircle-text-rightT-60").text(this.clientTonsrc["confident"] + "%");
            d3.select("#outercircle-text-rightT-30").text(this.clientTonsrc["tentative"] + "%");
            d3.select("#outercircle-text-rightT-0").text(this.clientTonsrc["fear"] + "%");
            d3.select("#outercircle-text-rightT-330").text(this.clientTonsrc["sadness"] + "%");
            d3.select("#outercircle-text-rightT-300").text(this.clientTonsrc["joy"] + "%");
            d3.select("#outercircle-text-rightT-270").text(this.clientTonsrc["anger"] + "%");
       
            d3.select("#outercircle-text-top-180").text(this.clientTonsrc["analytical"] + "%");
            d3.select("#outercircle-text-top-210").text(this.clientTonsrc["confident"] + "%");
            d3.select("#outercircle-text-top-240").text(this.clientTonsrc["tentative"] + "%");
            d3.select("#outercircle-text-top-270").text(this.clientTonsrc["fear"] + "%");
            d3.select("#outercircle-text-top-300").text(this.clientTonsrc["sadness"] + "%");
            d3.select("#outercircle-text-top-330").text(this.clientTonsrc["joy"] + "%");
            d3.select("#outercircle-text-top-360").text(this.clientTonsrc["anger"] + "%");

            d3.select("#outercircle-text-bottom-180").text(this.clientTonsrc["anlaytical"] + "%");
            d3.select("#outercircle-text-bottom-150").text(this.clientTonsrc["confident"] + "%");
            d3.select("#outercircle-text-bottom-120").text(this.clientTonsrc["tentative"] + "%");
            d3.select("#outercircle-text-botton-90").text(this.clientTonsrc["fear"] + "%");
            d3.select("#outercircle-text-bottom-60").text(this.clientTonsrc["sadness"] + "%");
            d3.select("#outercircle-text-bottom-30").text(this.clientTonsrc["joy"] + "%");
            d3.select("#outercircle-text-bottom-0").text(this.clientTonsrc["anger"] + "%");
       
                }
            });
        }
        this.cdRef.detectChanges();
    }
    //profile Details
    changRoute(name,tabName)
    {
        



        var  requestObj = new BaseContainer();
        requestObj['employeeName'] = this.dashboard['employeeName'];
        requestObj['noOfMail'] = this.dashboard['noOfMail'];
        requestObj['teambad'] = this.teamRelation['bad'];
        requestObj['teamgood'] = this.teamRelation['good'];
        requestObj['clientgood'] = this.clientRelation['good'];
        requestObj['clientbad'] = this.clientRelation['bad'];
        requestObj['department'] = this.dashboard['department'];
        requestObj['reportTo'] = this.dashboard['reportTo'];
        requestObj['noOfTeamMember'] = this.dashboard['noOfTeamMember'];
        this.sharedService.setData({'companyName':"",'companyIndex':0});
        // requestObj['companyIndex'] = this.companyIndex;
        this.companyIndex = 0;
        this.companyName = "";
        this.cdRef.detectChanges();
        this.router.navigate([name,requestObj]);
}

changRouteTouch(name){
    this.router.navigate([name]);
  }

changRouteDash(name,tabName)
    {
        this.sharedService.setData({'companyName':"",'companyIndex':0});
        this.companyIndex = 0;
        this.companyName = "";
        this.ngAfterViewInit();

    }

changRouteClient(name,tabName)
    {
        



        var  requestObj = new BaseContainer();
        requestObj['employeeName'] = this.dashboard['employeeName'];
        requestObj['noOfMail'] = this.dashboard['noOfMail'];
        requestObj['teambad'] = this.teamRelation['bad'];
        requestObj['teamgood'] = this.teamRelation['good'];
        requestObj['clientgood'] = this.clientRelation['good'];
        requestObj['clientbad'] = this.clientRelation['bad'];
        requestObj['department'] = this.dashboard['department'];
        requestObj['reportTo'] = this.dashboard['reportTo'];
        requestObj['noOfTeamMember'] = this.dashboard['noOfTeamMember'];
        requestObj['companyIndex'] = this.companyIndex;
        this.companyIndex = 0;
        this.companyName = "";
        this.router.navigate([name,requestObj]);
}

changRoutePersonal(name,tabName){

    this.companyIndex = 0;
    this.companyName = "";
    
    var  requestObj = new BaseContainer();
    var userId = this.dashboard['employeeId'];
    requestObj['userId'] = this.dashboard['employeeId'];
    this.router.navigate([name,userId]);
}

teamtoneemployeeAdverse(obj){

    var empId = this.dashboard['employeeId'];
    var adverseFilter = "yes";
    //this.sharedService.setData({'tesmscore':obj,'userId':empId, 'empHir': this.backList});
    this.companyIndex = 0;
    this.companyName = "";
    var  routObj = new BaseContainer();
    routObj['userId'] = empId; 
    routObj['adverseFilter'] = adverseFilter;
    this.router.navigate(['my-team-email',routObj]);
  }

  teamtoneemployeeEscalation(obj){

    var empId = this.dashboard['employeeId'];
    var escalationFilter = "yes";
    this.companyIndex = 0;
    this.companyName = "";
    //this.sharedService.setData({'tesmscore':obj,'userId':empId, 'empHir': this.backList});
    var  routObj = new BaseContainer();
    routObj['userId'] = empId; 
    routObj['escalationFilter'] = escalationFilter;
    this.router.navigate(['my-team-email',routObj]);
  }
  ngOnDestroy()
  {}

    
    onclickcircleemployee(obj)
    {
                 //console.log();
                 var  requestObj = new BaseContainer();
        requestObj['employeeName'] = this.dashboard['employeeName'];
        requestObj['noOfMail'] = this.dashboard['noOfMail'];
        requestObj['teambad'] = this.teamRelation['bad'];
        requestObj['teamgood'] = this.teamRelation['good'];
        requestObj['clientgood'] = this.clientRelation['good'];
        requestObj['clientbad'] = this.clientRelation['bad'];
        requestObj['department'] = this.dashboard['department'];
        requestObj['reportTo'] = this.dashboard['reportTo'];
        requestObj['noOfTeamMember'] = this.dashboard['noOfTeamMember'];
        
        this.resList.push(obj);
       this.sharedService.setData({'tabName':'employee', 'searchCriteria': this.resList, 'sortType': 'DSC', 'employeeId':this.empId});
       this.router.navigate(['my-employee-dashboard',requestObj]);
       
    }

    onclickcircleclient(obj)
    {
        var  requestObj = new BaseContainer();
        requestObj['employeeName'] = this.dashboard['employeeName'];
        requestObj['noOfMail'] = this.dashboard['noOfMail'];
        requestObj['teambad'] = this.teamRelation['bad'];        
        requestObj['teamgood'] = this.teamRelation['good'];
        requestObj['clientgood'] = this.clientRelation['good'];
        requestObj['clientbad'] = this.clientRelation['bad'];
        requestObj['department'] = this.dashboard['department'];
        requestObj['reportTo'] = this.dashboard['reportTo'];
        requestObj['noOfTeamMember'] = this.dashboard['noOfTeamMember'];
        requestObj['companyIndex'] = this.companyIndex;
        this.resclient = [];
         this.resclient.push(obj);
       this.sharedService.setData({'tabName':'client', 'searchCriteria': this.resclient, 'sortType': 'DSC','employeeId':this.empId,'companyIndex':this.companyIndex});
       this.router.navigate(['my-client-dashboard',requestObj]);
       
    }

}
