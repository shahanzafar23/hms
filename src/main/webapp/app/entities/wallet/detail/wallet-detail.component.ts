import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IAdminUser } from 'app/entities/user/user-management.model';
import { UserService } from 'app/entities/user/user.service';

import { IWallet } from '../wallet.model';

@Component({
  selector: 'jhi-wallet-detail',
  templateUrl: './wallet-detail.component.html',
})
export class WalletDetailComponent implements OnInit {
  wallet: IWallet | null = null;
  users: IAdminUser[] = [];
  type: string | undefined;

  constructor(protected activatedRoute: ActivatedRoute, private userService: UserService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wallet }) => {
      this.wallet = wallet;
    });
    this.activatedRoute.params.forEach(element => {
      const type:string = element['type'];
      this.type = type;
      this.userService.queryForType(type).subscribe(response => {
        if(response.body) {
          response.body.forEach(user => {
            this.users.push(user);
          });
        }
      });
    })
    if(this.users.length === 0) {
      this.userService.queryForType('all').subscribe(response => {
        if(response.body) {
          response.body.forEach(user => {
            this.users.push(user);
          });
        }
      });
    }
  }

  getUserName(userId: string | undefined | null): string {
    console.log(this.users);
    let loginName = ''; 
    this.users.forEach((element) => {
      if (element.id === userId && element.login) {
         loginName = element.login;
      }
    });
    return loginName;
  }

  previousState(): void {
    window.history.back();
  }
}
