import {USER_ROLE, UserRole} from './enum/user-role';
import {UserStatus} from './enum/user-status';

export class User {
  /** id */
  id: number | undefined;

  /**
   * 密码
   */
  password: string | undefined;

  /**
   * 姓名
   */
  name: string | undefined;

  /**
   * 工号/学号
   */
  num: string | undefined;

  /**
   * 状态
   */
  status: UserStatus | undefined;

  /**
   * 用户名(手机号）
   */
  username: string | undefined;

  constructor(data = {} as {
    id?: number,
    password?: string,
    name?: string,
    num?: string,
    status?: UserStatus,
    username?: string,
  }) {
    this.id = data.id;
    this.password = data.password;
    this.username = data.username;

    this.status = data.status;
    this.name = data.name;
    this.num = data.num;
  }
}
