import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {CatalogService} from "./catalog.service";
import "rxjs/add/operator/switchMap";
import {Catalog} from "./catalog";
import {DatasetService} from "../dataset/dataset.service";
import {Dataset} from "../dataset/dataset";


@Component({
  selector: 'app-catalog',
  templateUrl: './catalog.component.html',
  styleUrls: ['./catalog.component.css']
})
export class CatalogComponent implements OnInit {

  catalog: Catalog;
  datasets: Dataset[];
  title: string;
  description: string;
  language: string;
  saved: boolean;


  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: CatalogService,
    private datasetService: DatasetService
  ) { }

  ngOnInit() {
    this.language = 'nb';
    // snapshot alternative
    let id = this.route.snapshot.params['cat_id'];
    this.service.get(id).then((catalog: Catalog) => this.catalog = catalog);
    this.datasetService.getAll(id).then((datasets: Dataset[]) => this.datasets = datasets);
  }

  save(): void {
    this.service.save(this.catalog)
      .then(() => this.saved = true)
  }

}