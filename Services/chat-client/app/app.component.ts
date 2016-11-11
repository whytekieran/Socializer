import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
    selector: 'core-app',
    templateUrl: '/app/app.component.html',
    styleUrls: ['/app/app.component.css']
})

export class AppComponent{
    title = 'Socializer';
    public location = '';
    
    constructor(
        private router: Router
    ){
        this.location = router.url;
    }

    isActivePath(activePath: string): boolean{
        return location.pathname == activePath;
    }
}