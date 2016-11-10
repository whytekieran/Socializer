import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { Ng2BootstrapModule } from 'ng2-bootstrap/ng2-bootstrap';

import { AppComponent }       from './app.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { IndexComponent }     from './index/index.component';

import { AppRoutingModule }     from './routing/app-routing.module';

@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        Ng2BootstrapModule,
        AppRoutingModule
    ],
    declarations: [
        AppComponent,
        IndexComponent,
        DashboardComponent,
    ],
    providers: [
        
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }