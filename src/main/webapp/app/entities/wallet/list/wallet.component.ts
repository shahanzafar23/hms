import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IWallet } from '../wallet.model';
import { WalletService } from '../service/wallet.service';
import { WalletDeleteDialogComponent } from '../delete/wallet-delete-dialog.component';
import { ActivatedRoute } from '@angular/router';
import { UserService } from 'app/entities/user/user.service';
import { IAdminUser } from 'app/entities/user/user-management.model';

@Component({
  selector: 'jhi-wallet',
  templateUrl: './wallet.component.html',
})
export class WalletComponent implements OnInit {
  wallets?: IWallet[];
  users: IAdminUser[] = [];
  isLoading = false;
  type = '';

  constructor(protected walletService: WalletService, protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute, private userService: UserService) {}

  loadAll(): void {
    this.isLoading = true;
    this.activatedRoute.params.forEach(element => {
      const type:string = element['type'];
      this.type = type;
      if(type){
        this.walletService.queryForType(`${type}`).subscribe({
          next: (res: HttpResponse<IWallet[]>) => {
            this.isLoading = false;
            console.log(res.body);
            this.wallets = res.body ?? [];
          },
          error: () => {
            this.isLoading = false;
          },
        });
        this.userService.queryForType(type).subscribe(response => {
          if(response.body) {
            response.body.forEach(user => {
              this.users.push(user);
            });
            
          }
        });
      }
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IWallet): string {
    return item.id!;
  }

  getUserName(userId: string | undefined | null): string {
    let loginName = ''; 
    this.users.forEach((element) => {
      if (element.id === userId && element.login) {
         loginName = element.login;
      }
    });
    return loginName;
  }

  delete(wallet: IWallet): void {
    const modalRef = this.modalService.open(WalletDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.wallet = wallet;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
