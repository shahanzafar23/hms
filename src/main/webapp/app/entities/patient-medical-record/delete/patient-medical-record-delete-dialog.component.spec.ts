jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { PatientMedicalRecordService } from '../service/patient-medical-record.service';

import { PatientMedicalRecordDeleteDialogComponent } from './patient-medical-record-delete-dialog.component';

describe('PatientMedicalRecord Management Delete Component', () => {
  let comp: PatientMedicalRecordDeleteDialogComponent;
  let fixture: ComponentFixture<PatientMedicalRecordDeleteDialogComponent>;
  let service: PatientMedicalRecordService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PatientMedicalRecordDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(PatientMedicalRecordDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PatientMedicalRecordDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PatientMedicalRecordService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete('ABC');
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith('ABC');
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
