import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPatientMedicalRecord } from '../patient-medical-record.model';
import { PatientMedicalRecordService } from '../service/patient-medical-record.service';
import { PatientMedicalRecordDeleteDialogComponent } from '../delete/patient-medical-record-delete-dialog.component';
import { IPatientMedicalHistory } from '../patient-medical-history.model';
import { FormBuilder } from '@angular/forms';
import { WalletService } from 'app/entities/wallet/service/wallet.service';
import { IWallet } from 'app/entities/wallet/wallet.model';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-patient-medical-record',
  templateUrl: './patient-medical-record.component.html',
})
export class PatientMedicalRecordComponent implements OnInit {
  patientMedicalRecords?: IPatientMedicalRecord[];
  patientMedialHistory?: IPatientMedicalHistory[];
  isLoading = false;
  searchForm = this.fb.group({
    search: [],
  });
  disableSearch = false;
  wallets: IWallet[] = [];

  constructor(protected patientMedicalRecordService: PatientMedicalRecordService, 
    protected modalService: NgbModal, private fb: FormBuilder, private walletService: WalletService, private accountService: AccountService) {}

  loadAll(): void {
    this.isLoading = true;
    this.accountService.getAuthenticationState().subscribe(account => {
      if(this.wallets.length ===0 && account && this.accountService.hasAnyAuthority('ROLE_PATIENT')) {
        this.searchForm.patchValue({
          search: account.email
        });
        this.disableSearch = true;
        this.search();
      }
    })
    if(this.wallets.length > 0) {
      this.patientMedicalRecordService.queryForAll(this.wallets[0]?.id ?? "").subscribe({
        next: (res: HttpResponse<IPatientMedicalHistory[]>) => {
          this.isLoading = false;
          this.patientMedialHistory = res.body ?? [];
        },
        error: () => {
          this.isLoading = false;
        },
      });
    }
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPatientMedicalRecord): string {
    return item.id!;
  }

  isSearchDisabled(): boolean {
    console.log(this.disableSearch);
    return this.disableSearch;
  }

  delete(patientMedicalRecord: IPatientMedicalRecord): void {
    const modalRef = this.modalService.open(PatientMedicalRecordDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.patientMedicalRecord = patientMedicalRecord;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  search(): void {
    this.walletService.queryForPatientLoginOrEmail(this.searchForm.get(['search'])!.value).subscribe({
      next: (res: HttpResponse<IWallet[]>) => {
        this.isLoading = false;
        this.wallets = res.body ?? [];
        this.loadAll();
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }
}
