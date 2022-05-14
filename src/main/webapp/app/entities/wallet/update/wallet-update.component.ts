import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IWallet, Wallet } from '../wallet.model';
import { WalletService } from '../service/wallet.service';
import { UserService } from 'app/entities/user/user.service';
import { IAdminUser } from 'app/entities/user/user-management.model';

@Component({
  selector: 'jhi-wallet-update',
  templateUrl: './wallet-update.component.html',
})
export class WalletUpdateComponent implements OnInit {
  isSaving = false;
  userIds: IAdminUser[] = [];
  type: string | undefined;

  editForm = this.fb.group({
    id: [],
    street: [],
    city: [],
    state: [],
    country: [],
    postCode: [],
    age: [],
    phone: [],
    publicKey: [],
    privateKey: [],
    userId: [],
    hname: []
  });

  constructor(protected walletService: WalletService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder, protected userService: UserService) {}

  ngOnInit(): void {
    this.activatedRoute.params.forEach(element => {
      const type:string = element['type'];
      this.type = type;
      this.userService.queryForType(type).subscribe(response => {
        if(response.body) {
          response.body.forEach(user => {
            this.userIds.push(user);
          });
        }
      })
      });
    this.activatedRoute.data.subscribe(({ wallet }) => {
      this.updateForm(wallet);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const wallet = this.createFromForm();
    if (wallet.id !== undefined) {
      this.subscribeToSaveResponse(this.walletService.update(wallet));
    } else {
      this.subscribeToSaveResponse(this.walletService.create(wallet));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWallet>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(wallet: IWallet): void {
    this.editForm.patchValue({
      id: wallet.id,
      street: wallet.street,
      city: wallet.city,
      state: wallet.state,
      country: wallet.country,
      postCode: wallet.postCode,
      age: wallet.age,
      phone: wallet.phone,
      publicKey: wallet.publicKey,
      privateKey: wallet.privateKey,
      userId: wallet.userId,
      hname: wallet.hospitalName
    });
  }

  protected createFromForm(): IWallet {
    return {
      ...new Wallet(),
      id: this.editForm.get(['id'])!.value,
      street: this.editForm.get(['street'])!.value,
      city: this.editForm.get(['city'])!.value,
      state: this.editForm.get(['state'])!.value,
      country: this.editForm.get(['country'])!.value,
      postCode: this.editForm.get(['postCode'])!.value,
      age: this.editForm.get(['age'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      publicKey: this.editForm.get(['publicKey'])!.value,
      privateKey: this.editForm.get(['privateKey'])!.value,
      userId: this.editForm.get(['userId'])!.value,
      hospitalName: this.editForm.get(['hname'])!.value
    };
  }
}
