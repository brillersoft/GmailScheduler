import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { HttpClientModule } from "@angular/common/http";
import { FormsModule } from "@angular/forms";
import { HttpModule } from "@angular/http";
import { ReactiveFormsModule } from "@angular/forms";

import { FileSelectDirective, FileDropDirective } from "ng2-file-upload";
import { Ng2AutoCompleteModule } from "ng2-auto-complete";
import { ModalModule } from "angular2-modal";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations"; // this is needed!
import { ChartsModule } from "ng2-charts";
import { AppRoutingModule } from "./app-routing.module";
import { HttpService } from "../service/http.service";
import { SharedService } from "../service/shared-data-service";
import { JobsService } from "../service/JobsService-service";
import { RoleGuardService } from "../service/role-guard.service";

import { EmployeeAppComponent } from "./employeeapp/employeeapp.component";
import { AdminDashboard } from "./admin/admindashboard";
import { Employeeupload } from "./admin/employeeupload";
import { EmployeeData } from "./admin/employeedata";
import { ClientData } from "./admin/clientdata";
import { ScheduleComponent } from "./admin/schedule.component";
import { ServerConfiguration } from "./admin/emailserver.configuration";
import { ClientUpload } from "./admin/clientupload";
import { OrganizationDetails } from "./admin/organizationdetails";
import { SignUp } from "./admin/signup.component";
import { RegisterComponent } from "./admin/register.component";
import { PersonalityAnalyticsComponent } from "./personality-analytics/PersonalityAnalytics.component";
import { RelationshipComponent } from "./relationship/Relationship.component";
import { ResponseTimeComponent } from "./response-time/ResponseTime.component";

import { DashboardComponent } from "./dashboard/dashboard.component";
import { EmployeeDashboardComponent } from "./employeedashboard/employeedashboard.component";
import { ClientDashboardComponent } from "./clientdashboard/clientdashboard.component";
import { EmployeePersonalScoreComponent } from "./employeepersonalscore/employeepersonalscore.component";
import { TeamEmailComponent } from "./teamemail/teamemail.component";
import { TeamScoreComponent } from "./teamScore/teamScore.component";
import { BargraphComponent } from "./bargraph/bargraph.component";
import { ChartdataService } from "./bargraph/chartdata.service";
import { TouchPointsComponent } from "./touchPoints/touchPoints.component";
import { UiSwitchModule } from "ngx-toggle-switch";
import { NgxPaginationModule } from "ngx-pagination";
import { NotifierModule, NotifierOptions } from "angular-notifier";
import { UserIdleModule } from "angular-user-idle";
import { MatTabsModule } from "@angular/material/tabs";
import { MatList, MatListModule } from "@angular/material/list";
import { MatTooltipModule } from "@angular/material/tooltip";
// import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';
// import { NgxMaterialTimepickerModule } from "ngx-material-timepicker";
// import {MatDrawer, MatDrawerContainer, MatDrawerContent} from '@angular/material/dr';
import {
  MatSidenav,
  MatSidenavContent,
  MatSidenavModule,
  MatDrawer
} from "@angular/material/sidenav";
import { MatIconModule } from "@angular/material/icon";
// import { NgxCountrySelectModule } from 'ngx-country-select';

// import { MaterialModule } from './material.module';

const notifierDefaultOptions: NotifierOptions = {
  position: {
    horizontal: {
      position: "right",
      distance: 12
    },
    vertical: {
      position: "top",
      distance: 12,
      gap: 10
    }
  },
  theme: "material",
  behaviour: {
    autoHide: 5000,
    onClick: false,
    onMouseover: "pauseAutoHide",
    showDismissButton: true,
    stacking: 4
  },
  animations: {
    enabled: true,
    show: {
      preset: "slide",
      speed: 300,
      easing: "ease"
    },
    hide: {
      preset: "fade",
      speed: 300,
      easing: "ease",
      offset: 50
    },
    shift: {
      speed: 300,
      easing: "ease"
    },
    overlap: 150
  }
};

@NgModule({
  declarations: [
    EmployeeAppComponent,
    DashboardComponent,
    EmployeeDashboardComponent,
    ClientDashboardComponent,
    EmployeePersonalScoreComponent,
    TeamEmailComponent,
    TeamScoreComponent,
    BargraphComponent,
    TouchPointsComponent,
    AdminDashboard,
    Employeeupload,
    EmployeeData,
    ClientData,
    ClientUpload,
    ScheduleComponent,
    ServerConfiguration,
    OrganizationDetails,
    SignUp,
    RegisterComponent,
    PersonalityAnalyticsComponent,
    RelationshipComponent,
    // OwlDateTimeModule, 
    // OwlNativeDateTimeModule,
    ResponseTimeComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    Ng2AutoCompleteModule,
    HttpModule,
    ModalModule.forRoot(),
    ChartsModule,
    NgxPaginationModule,
    UiSwitchModule,
    MatTabsModule,
    MatSidenavModule,
    MatIconModule,
    MatListModule,
    MatTooltipModule,
    UserIdleModule.forRoot({ idle: 1200, timeout: 1, ping: 120 }),
    // NgxMaterialTimepickerModule.forRoot(),
    //  NgxCountrySelectModule,
    NotifierModule.withConfig(notifierDefaultOptions)
  ],
  providers: [HttpService, SharedService, ChartdataService, JobsService, RoleGuardService],
  bootstrap: [EmployeeAppComponent]
})
export class EmployeeModule {}
