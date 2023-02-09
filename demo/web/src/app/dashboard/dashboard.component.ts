import {Component, OnInit} from '@angular/core';
import {environment} from '../../environments/environment';
import {User} from '../../entity/user';
import {UserService} from '../../service/user.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  title = environment.title;

  isStudent = false;

  isTeacher = false;

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
    this.userService.getCurrentLoginUser$()
      .subscribe((user: User) => {
        this.isTeacher = true;
        }
      );
  }

}
