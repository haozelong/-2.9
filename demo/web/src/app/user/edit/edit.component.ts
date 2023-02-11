import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {CommonService} from "../../../service/common.service";
import {ActivatedRoute} from "@angular/router";
import {User} from '../../../entity/user';
import {UserService} from '../../../service/user.service';

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.scss']
})
export class EditComponent implements OnInit {

  formGroup = new FormGroup({});
  formKeys = {
    username: 'username',
    name: 'name',
  };
  // @ts-ignore
  @ViewChild('home')
  htmlAnchorRef: ElementRef | undefined;
  user = new User();

  constructor(private commonService: CommonService,
              private route: ActivatedRoute,
              private userService: UserService) {
  }

  loadById(id: number): void {
    this.userService.getById(id)
      .subscribe(clazz => {
        this.setUser(clazz);
      });
  }

  ngOnInit(): void {
    this.formGroup.addControl(this.formKeys.username, new FormControl('', Validators.required));
    this.formGroup.addControl(this.formKeys.name, new FormControl('', Validators.required));

    this.route.params.subscribe(param => {
      const id = param.id;
      console.log(id);
      if (id !== null && id !== undefined) {
        console.log(id);
        this.loadById(+id);
      }
    });
  }

  onSubmit(formGroup: FormGroup): void {
    const user = new User({
      username: formGroup.get(this.formKeys.username).value,
      name: formGroup.get(this.formKeys.name).value
    });
    this.userService.update(this.user.id!, user)
      .subscribe(() => {
        this.commonService.success(() => {
          this.commonService.back();
        });
      });
  }

  setUser(user: User): void {
    this.user = user;
    this.formGroup.get(this.formKeys.username).setValue(user.username);
    this.formGroup.get(this.formKeys.name).setValue(user.name);
  }

}
