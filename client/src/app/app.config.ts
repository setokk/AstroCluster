import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { ClusterService } from './core/services/cluster-service';
import { provideHttpClient } from '@angular/common/http';
import { FormsModule, NgForm } from '@angular/forms';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }), 
    provideRouter(routes),
    ClusterService,
    provideHttpClient(),
    FormsModule,
    NgForm
  ]
};
