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
  metadata: any = {};
  encodeURIComponent = encodeURIComponent;

  constructor(private route: ActivatedRoute, private http: HttpClient, private router: Router, private auth: AuthenticationService, public userService: UserService) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      if (params['code']) {
        this.http.post('http://localhost:8080/front/login', { code: params['code'], url: 'http://localhost:4200' }, { responseType: 'text' }).subscribe( {
          next:(response) => {
            // Handle the response here
            console.log(response);
            this.auth.token.next(response);
            this.http.get('http://localhost:8080/api/certificate/').subscribe(a=>console.log(a));
          },
          error:(error) => {
            // Handle errors here
            console.error(error);
          },
        }).add(() => {
          this.router.navigate(
            ['.'], 
            { relativeTo: this.route}
          );
        });      }
    });
    // console.log(this.auth);
    // this.auth.user$
    // // .pipe(
    // //   concatMap((user: User | any) =>
    // //     // Use HttpClient to make the call
    // //     this.http.get(
    // //       encodeURI(`https://{yourDomain}/api/v2/users/${user.sub}`)
    // //     )
    // //   ),
    // //   map((user: User) => user['user_metadata']),
    // //   tap((meta: any) => (this.metadata = meta))
    // // )
    // .subscribe((user)=>{
    //   console.log(user);
    // });
  }
}
