import { Component, OnInit } from '@angular/core';
import { concatMap, map, tap } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService, TokenInterceptor, UserService } from '../auth-service/authentication.service';


@Component({
  selector: 'app-user-info',
  templateUrl: './user-info.component.html',
  styleUrls: ['./user-info.component.css']
})
export class UserInfoComponent implements OnInit {
  window = window;
  hover = false;
  metadata: any = {};
  encodeURIComponent = encodeURIComponent;

  constructor(public userService: UserService, private router : Router) { }

  ngOnInit(): void {
  }

  certificatesOverview() {
    this.router.navigate(["certificates"]);
  }
}
