import { Injectable } from '@angular/core';
import { 
  Router,
  CanActivate,
  ActivatedRouteSnapshot
} from '@angular/router';
// import { AuthService } from './auth.service';
// import * as decode from 'jwt-decode';
import { SharedService } from './shared-data-service';

@Injectable()
export class RoleGuardService implements CanActivate {


  constructor(
    private sharedService:SharedService,
    public router: Router) {}


  canActivate(route: ActivatedRouteSnapshot): boolean {
    // this will be passed from the route config
    // on the data property
    // const expectedRole = route.data.expectedRole;
    // const token = localStorage.getItem('token');
    // decode the token to get its payload
    // const tokenPayload = decode(token);
    const globalData = this.sharedService.getData();
    if (
      globalData.role != "admin"
    ) {
      this.router.navigate(['my-main-dashboard']);
      return false;
    }
    return true;
  }
}