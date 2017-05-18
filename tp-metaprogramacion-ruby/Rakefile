require 'rspec/core/rake_task'

RSpec::Core::RakeTask.new(:spec)

desc 'Ejecutando los tests'
Rake::TestTask.new('test_units') do |t|
  t.pattern = 'test/test_*.rb' # Busca los archivos comenzados en 'test_.rb'
  t.verbose = true
  t.warning = true
end

task :default => [:test_units]
