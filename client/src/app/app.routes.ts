import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { AboutComponent } from './about/about.component';
import { AnalysisComponent } from './analysis/analysis.component';
import { HistoryComponent } from './history/history.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';

export const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: 'what-is-astrocluster', component: AboutComponent },
    { path: 'analysis', component: AnalysisComponent },
    { path: 'history', component: HistoryComponent },
    { path: 'log-in', component: LoginComponent },
    { path: 'register', component: RegisterComponent }
];
