import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {CommonService} from "../../../service/common.service";
import {UserService} from '../../../service/user.service';
import {User} from '../../../entity/user';

@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.scss']
})
export class AddComponent implements OnInit {

  formGroup = new FormGroup({});
  formKeys = {
    username: 'username',
    name: 'name',
  };
  // @ts-ignore
  @ViewChild('home')
  htmlAnchorRef: ElementRef | undefined;

  constructor(private commonService: CommonService,
              private userService: UserService) {
  }

  ngOnInit(): void {
    this.formGroup.addControl(this.formKeys.username, new FormControl('', Validators.required));
    this.formGroup.addControl(this.formKeys.name, new FormControl('', Validators.required));
  }

  onSubmit(formGroup: FormGroup): void {
    const user = new User({
        username: formGroup.get(this.formKeys.username).value,
        name: formGroup.get(this.formKeys.name).value
    });

    this.userService.save(user)
      .subscribe(() => {
        this.commonService.success(() => {
          this.commonService.back();
        });
      });
  }

}
