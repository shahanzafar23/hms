import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PatientMedicalRecordDetailComponent } from './patient-medical-record-detail.component';

describe('PatientMedicalRecord Management Detail Component', () => {
  let comp: PatientMedicalRecordDetailComponent;
  let fixture: ComponentFixture<PatientMedicalRecordDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PatientMedicalRecordDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ patientMedicalRecord: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(PatientMedicalRecordDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PatientMedicalRecordDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load patientMedicalRecord on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.patientMedicalRecord).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
