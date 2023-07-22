import { Component } from '@angular/core';
import { UserService } from '../auth-service/authentication.service';

@Component({
  selector: 'app-index-page',
  templateUrl: './index-page.component.html',
  styleUrls: ['./index-page.component.css']
})
export class IndexPageComponent {
  sent: boolean = false;
  code: string = "";

  constructor(public userService: UserService) {}
}
