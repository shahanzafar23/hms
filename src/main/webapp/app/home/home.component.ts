import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { WalletService } from 'app/entities/wallet/service/wallet.service';
import { HttpResponse } from '@angular/common/http';
import { IWallet } from 'app/entities/wallet/wallet.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  totalDoctors = 0;
  totalPatients = 0;
  totalInsurer = 0;
  totalERs = 0;
  totalUsers = 0;

  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService, private router: Router, private walletService: WalletService, private userService: UserService) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));

    this.walletService.queryForType(`doctor`).subscribe({
        next: (res: HttpResponse<IWallet[]>) => {
          this.totalDoctors = res.body?.length ?? 0;
        }
      });
    this.walletService.queryForType(`patient`).subscribe({
        next: (res: HttpResponse<IWallet[]>) => {
          this.totalPatients = res.body?.length ?? 0;
        }
      });
    this.walletService.queryForType(`insurer`).subscribe({
        next: (res: HttpResponse<IWallet[]>) => {
          this.totalInsurer = res.body?.length ?? 0;
        }
      });
    this.walletService.queryForType(`er`).subscribe({
        next: (res: HttpResponse<IWallet[]>) => {
          this.totalERs = res.body?.length ?? 0;
        }
      });   
    this.userService.queryForType('all').subscribe(response => {
        if(response.body) {
          this.totalUsers = response.body.length;
        }
      });     
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
