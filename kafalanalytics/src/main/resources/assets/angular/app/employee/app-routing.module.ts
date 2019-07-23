import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { DashboardComponent } from "./dashboard/dashboard.component";
import { EmployeeDashboardComponent } from "./employeedashboard/employeedashboard.component";
import { ClientDashboardComponent } from "./clientdashboard/clientdashboard.component";
import { EmployeePersonalScoreComponent } from "./employeepersonalscore/employeepersonalscore.component";
import { TeamEmailComponent } from "./teamemail/teamemail.component";
import { TeamScoreComponent } from "./teamScore/teamScore.component";
import { BargraphComponent } from "./bargraph/bargraph.component";
import { TouchPointsComponent } from "./touchPoints/touchPoints.component";
import { PersonalityAnalyticsComponent } from "./personality-analytics/PersonalityAnalytics.component";
import { RelationshipComponent } from "./relationship/Relationship.component";
import { ResponseTimeComponent } from "./response-time/ResponseTime.component";
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
import { RoleGuardService as RoleGuard } from "../service/role-guard.service";

const routes: Routes = [
  { path: "", component: BargraphComponent },
  { path: "my-dashboard", component: DashboardComponent },
  { path: "my-employee-dashboard", component: EmployeeDashboardComponent },
  { path: "my-client-dashboard", component: ClientDashboardComponent },
  {
    path: "my-employee-personal-score/:userId",
    component: EmployeePersonalScoreComponent
  },
  { path: "my-team-email", component: TeamEmailComponent },
  { path: "my-team-score/:userId", component: TeamScoreComponent },
  { path: "my-main-dashboard", component: BargraphComponent },
  { path: "touch-points", component: TouchPointsComponent },
  { path: "response-time", component: ResponseTimeComponent },
  { path: "personality-analytics", component: PersonalityAnalyticsComponent },
  { path: "relationship", component: RelationshipComponent },
  { path: "admin-dashboard/:userId", component: AdminDashboard },
  { path: "employee-upload/:userId", component: Employeeupload, canActivate: [RoleGuard] },
  { path: "employee-data/:userId", component: EmployeeData, canActivate: [RoleGuard] },
  { path: "client-data/:userId", component: ClientData, canActivate: [RoleGuard] },
  { path: "server-configuration/:userId", component: ServerConfiguration, canActivate: [RoleGuard] },
  { path: "schedule-batch/:userId", component: ScheduleComponent, canActivate: [RoleGuard] },
  { path: "client-upload/:userId", component: ClientUpload, canActivate: [RoleGuard] },
  { path: "organization-details", component: OrganizationDetails, canActivate: [RoleGuard] },
  { path: "test-route", component: SignUp },
  { path: "register-organization", component: RegisterComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule],
  declarations: []
})
export class AppRoutingModule {}
