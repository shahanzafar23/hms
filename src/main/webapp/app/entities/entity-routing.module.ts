import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'wallet',
        data: { pageTitle: 'Wallets'},
        loadChildren: () => import('./wallet/wallet.module').then(m => m.WalletModule),
      },
      {
        path: 'patient-medical-record',
        data: { pageTitle: 'PatientMedicalRecords' },
        loadChildren: () => import('./patient-medical-record/patient-medical-record.module').then(m => m.PatientMedicalRecordModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
