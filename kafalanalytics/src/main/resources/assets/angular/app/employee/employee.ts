import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { EmployeeModule } from './employee.module';
import { environment } from '../../environments/environment';

import 'bootstrap';

if (environment.production) {
  enableProdMode();
}

platformBrowserDynamic().bootstrapModule(EmployeeModule);
