import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditComponent } from './edit.component';
import {ReactiveFormsModule} from "@angular/forms";
import {ApiTestingModule} from "../../../../api/api.testing.module";
import {ActivatedRoute} from "@angular/router";
import {ActivatedRouteStub, RouterTestingModule} from "@yunzhi/ng-router-testing";
import {randomNumber} from "@yunzhi/ng-mock-api";
import {getTestScheduler} from "jasmine-marbles";

describe('EditComponent', () => {
  let component: EditComponent;
  let fixture: ComponentFixture<EditComponent>;
  let route: ActivatedRouteStub;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditComponent ],
      imports: [
        RouterTestingModule,
        ReactiveFormsModule,
        ApiTestingModule,
        RouterTestingModule,
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditComponent);
    component = fixture.componentInstance;
    route = TestBed.inject(ActivatedRoute) as unknown as ActivatedRouteStub;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    // 该组件依赖于路由，预使其初始化，必须手动发送路由数据
    route.paramsSubject.next({id: randomNumber().toString(10)});
    fixture.autoDetectChanges();
  });

  it('ngOnInit', () => {
    expect(component).toBeTruthy();
    route.paramsSubject.next({id: randomNumber().toString(10)});
    fixture.autoDetectChanges();
    getTestScheduler().flush();
    fixture.detectChanges();
  });

  it('findById', () => {
    spyOn(component, 'setUser');
    component.loadById(randomNumber());
    // 马上发送mockApi的数据
    getTestScheduler().flush();
    expect(component.setUser).toHaveBeenCalled();
  });
});
